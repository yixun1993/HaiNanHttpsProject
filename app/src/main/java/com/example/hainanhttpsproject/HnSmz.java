package com.example.hainanhttpsproject;

import Impl.HnSmzImpl;
import Impl.HnSmzSdkListner;
import Impl.Request_https;
import JsonBean.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.example.hainanhttpsproject.Dao.DbManger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.DesUtil;
import util.HttpMethod;

import java.lang.ref.WeakReference;
import java.nio.charset.Charset;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.functions.Consumer;
import util.RxScheduler;

public class HnSmz implements HnSmzImpl {
    private static Gson GSON = new GsonBuilder().create();

    //弱应用
    private WeakReference<Context> contextWeakReference;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    //在自己内部定义自己的一个实例，只供内部调用
    private static final HnSmz instance = new HnSmz();
    private HnSmzSdkListner mHnSmzSdkListner;
    private Uploadlogs uploadlogsTemp;
    // private final String desKey = "48994686"; //  测试
//    private String desKey = "41145408"; // 正式
    private String mDesKey ; // 正式
    private String mSn;
    private boolean timelock = false;
    private long lastTime;
    private String mMsg;
    private HnSmz(){
    }
    //这里提供了一个供外部访问本class的静态方法，可以直接访问
    public static HnSmz getInstance(){
        return instance;
    }

    @Override
    public HnSmz InitSMZ(Context context) {
        contextWeakReference = new WeakReference<>(context);
        DbManger.getInstance().InitDb(contextWeakReference.get());
        return this;
    }


    @Override
    public HnSmz setSn(String sn) {
        this.mSn = sn;
        return this;
    }

    @Override
    public HnSmz setDesKey(String desKey) {
        this.mDesKey = desKey;
        return this;
    }

    @Override
    public HnSmz setHnSmzSdkListner(HnSmzSdkListner hnSmzSdkListner) {
         this.mHnSmzSdkListner = hnSmzSdkListner;
        return this;
    }

    @Override
    public void addErrorPerson(ErrorPerson errorPerson) {
       DbManger.getInstance().addErrorPserson(errorPerson);
    }

    @Override
    public void build() {
        //同步下发

        Flowable.interval(1000, 5 * 60 * 1000, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //同步下发
                         getAddPerson();
                        //删除
                         getDel();

                    }
                });

        Flowable.interval(1000,  10 * 60 * 1000, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //上传识别结果第二步上传识别结果成功后清除考勤数据
                        uploadAttendanceData();
                    }
                });
      //上传识别结果第一步保存考勤数据
//        addUpload();
    }
    //上传识别结果第一步保存考勤数据
    public  void addUpload(String mSn,String user_id){
        Date nowTime = new Date();
        String dateString = df.format(nowTime);
        Uploadlogs uploadlogs = new Uploadlogs();
        uploadlogs.setRecog_time(dateString);
        uploadlogs.setRecog_type("iris");
        uploadlogs.setSn(mSn);
        uploadlogs.setUser_id(user_id);
        if (uploadlogsTemp == null) {
            uploadlogsTemp = uploadlogs;
        } else {
            //如果进出方向和ID一致认为是同一个人多次考勤
            if (uploadlogsTemp.getUser_id().equals(uploadlogs.getUser_id())) {
                timelock = true;
                if (System.currentTimeMillis() - lastTime > 3000) {
                    uploadlogsTemp = null;
                    timelock = false;
                }
            } else {
                uploadlogsTemp = uploadlogs;
                lastTime = System.currentTimeMillis();
            }
        }
        if (!timelock) {//存放本地数据库
            DbManger.getInstance().addUploadLogsBo(uploadlogs);
        }
    }
    //同步下发
    public  void getAddPerson(){
          Request_https httpAddPeson =  HttpMethod.getInstance().config();
          httpAddPeson.getAddPerson(mSn).enqueue(new Callback<Object>() {
              @Override
              public void onResponse(Call<Object> call, Response<Object> response) {
                  // 步骤7：处理返回的数据结果
                  if(response.isSuccessful()){
                      response.body().toString();
                      System.out.println("###数据HnSmz=="+response.body());
                      try {
                          JSONObject obj = new JSONObject(response.body().toString());
                          String keyContent  =    obj.optString("Content");
                          Log.e("a","getAddPserson=====keyContent==="+keyContent);
                          //判断是否下发成功
                          if(obj.getInt("Result") == 0){
                              if(keyContent != null && !("").equals(keyContent)){
                                  String decryptbyte =   DesUtil.decrypt( keyContent, Charset.forName("utf-8"),mDesKey);  //将加密后返回的字节行解密
                                  Gson gson1=new Gson();
                                  List<GetAddPerson> list= gson1.fromJson(decryptbyte, new TypeToken<List<GetAddPerson>>() {}.getType());
                                  //每次清除数据
//                                  cleanHoist();
                                  for(GetAddPerson addPerson :list){
                                      logutil("addPerson","####addPersonHnSmz===："+ addPerson.getName() + "id=="+addPerson.getId()+"iris_template=="+addPerson.getIris_template() );
                                      GetAddPerson addPerson1 =DbManger.getInstance().queryPersonByID(addPerson.getUser_id());
                                      if(addPerson1 == null){
                                          //添加数据库
                                          DbManger.getInstance().addPerson(addPerson);
                                      }else{
                                          addPerson.setPid(addPerson1.getPid());
                                          DbManger.getInstance().updatePerson(addPerson);
                                      }

                                  }
                                  List<GetAddPerson> getAddPersonList =   DbManger.getInstance().findPeson();
                                  Log.e("getAddPersonList","getAddPersonList============="+ getAddPersonList.size());
                                  //注册
                                  Register(list);
                                  for ( GetAddPerson getAddPerson : getAddPersonList){
                                      Log.e("getAddPersonFind","getAddPersonFind============="+ getAddPerson.toString());
                                  }
                                  //下发成功
                                  getFeedBack(2,mSn,"人员下发成功");
                                  // 添加到数据库
                                  logutil("GetADD","####HnSmz解密后内容为："+ decryptbyte);
                              }
                           } else {
                              //下发失败
                              getFeedBack(3,mSn,"人员下发成功");
                              Log.e("getAppperson","###HnSmzgetAppperson==="+"下发失败");
                          }

                      } catch (JSONException e) {
                          e.printStackTrace();
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                      //对数据的处理操作
                  }else{
                      Log.e("excption","###HnSmz异步请求失败出现404");
                  }
              }

              @Override
              public void onFailure(Call<Object> call, Throwable t) {
                  Log.e("onFailure","###HnSmzonFailure连接失败==" +call.getClass());
                  Log.e("onFailure","###HnSmzo请求失败==" +t.toString());
              }
          });
    }

    private  void delFaceByUserId(String id){
        if(mHnSmzSdkListner != null ){
            //删除百度人脸库离职
            mHnSmzSdkListner.delFaceDataByUserId(id);
        }
    }
    private  void cleanHoist(){
        //删除全部百度库人脸数据

        List<GetAddPerson> listFind = DbManger.getInstance().findPeson();
        if (listFind != null && listFind.size() != 0) {
            mHnSmzSdkListner.cleanAllListner(listFind);
        }
    }
    //删除
    public  void getDel(){
        HttpMethod.getInstance().config().getDel(mSn).enqueue(new Callback<Object>() {
            public void onResponse(Call<Object> call, Response<Object> response) {
                // 步骤7：处理返回的数据结果
                response.body().toString();
                Log.e("mains","####HnSmz删除成功");
                try {
                    JSONObject obj = new JSONObject(response.body().toString());
                    Log.e("mains","####HnSmzobj=="+obj);
                    String keyDeleteContent  =    obj.getString("Content");
                    Log.e("Getrequest","###keyContent=="+keyDeleteContent);
                    if(obj.getInt("Result") == 0){
                        if (keyDeleteContent != null && !keyDeleteContent.equals("")){
                            String     deleByte =   DesUtil.decrypt( keyDeleteContent, Charset.forName("utf-8"),mDesKey);  //将加密后返回的字节行解密
                            Gson gson1=new Gson();
                            List<DelPerson> list= gson1.fromJson(deleByte, new TypeToken<List<DelPerson>>() {}.getType());
                            for(DelPerson delPerson :list){
                                logutil("delPerson","####delPerson===："+ delPerson.getUser_id());
                                //查询数据库对应数据
                                GetAddPerson addPerson =DbManger.getInstance().queryPersonByID(delPerson.getUser_id());
                                delFaceByUserId(delPerson.getUser_id());
                                DbManger.getInstance().deletePerson(addPerson);
                                //根据离职userId查询注册失败库中对应数据
                                ErrorPerson errorPerson = DbManger.getInstance().delErrorByUserId(delPerson.getUser_id());
                                if(errorPerson != null){
                                    //根据离职删除失败库中对应数据
                                     DbManger.getInstance().deleteErrorPserson(errorPerson);
                                }

                            }

                            getFeedBack(1,mSn,"人员删除成功");
                        }
                    } else {
                        Log.e("getDel","###HnSmzgetDel删除" +"删除失败");
                        getFeedBack(0,mSn,"人员删除失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("getDel","###HnSmzgetDel2连接失败==" +call.getClass());
                Log.e("getDelonFailure2","###HnSmzgetDel请求失败==" +t.toString());
            }
        });

    }
    //设备回馈
    public void getFeedBack(int typeId,String sn,String msg){
        FeeBackExample feeBackExample = new FeeBackExample();
        feeBackExample.setType(typeId);
        feeBackExample.setSn(sn);
        feeBackExample.setMsg(msg);
        mMsg = msg;
        HttpMethod.getInstance().config().getFeedBack(feeBackExample.getType(),feeBackExample.getSn(),feeBackExample.getMsg()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // 步骤7：处理返回的数据结果
                String s = response.body().toString();
                System.out.println("###encrypt=="+response.body());
                Log.e("mains","####HnSmz回馈结果信息"+mMsg);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("getFeedBack","###HnSmzgetFeedBack回馈连接失败==" +call.getClass());
                Log.e("getFeedBack","###HnSmzgetFeedBack回馈请求失败==" +t.toString());
            }
        });
    }
    //上传识别结果
    @Override
    public void uploadAttendanceData() {
        //加载考勤数据
        List<Uploadlogs> uploadlogsDataList  = DbManger.getInstance().queryUploadData();
        if (uploadlogsDataList != null && uploadlogsDataList.size() > 0){
            UploadAttendance uploadAttendance = new UploadAttendance();
            uploadAttendance.setCount(uploadlogsDataList.size());
            uploadAttendance.setLogs(uploadlogsDataList);
            String datajson = GSON.toJson(uploadAttendance);
            Log.e("datajson","###datajson==="+datajson);
            String  encrypt =  DesUtil.encrypt(datajson, Charset.forName("utf-8"),mDesKey);  //进行加密
            Log.e("encrypt","####encryptUpload==="+encrypt);
            HttpMethod.getInstance().config().getUpload(mSn,encrypt).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    // 步骤7：处理返回的数据结果
                    response.body().toString();
                    Log.e("mains2","####HnSmz2上传成功,清空考勤记录数据库");
                    DbManger.getInstance().deleteUploadData();
                }
                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("onFailure2","###HnSmzonFailure2连接失败==" +call.getClass());
                    Log.e("onFailure2","###o2请求失败==" +t.toString());
                }
            });
        } else {
             Log.e("mains","####没有考勤记录上传");
        }

    }
    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    //注册人脸
    private void Register(final List<GetAddPerson> listGetAddPersonList) {
        Log.e("Register","Register进入注册");
        RxScheduler.doOnIOThread(new RxScheduler.IOTask<Void>() {
            @Override
            public void doOnIOThread() {
                for (GetAddPerson getAddPerson : listGetAddPersonList) {
                    Log.d("Register","##用户id =" + getAddPerson.getUser_id() + " 用户姓名=" + getAddPerson.getName());
                   if (mHnSmzSdkListner != null) {
                        String facephoto = getAddPerson.getFace_template();
                        if (!TextUtils.isEmpty(facephoto)) {
                           // Bitmap bitmap =BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap-hdpi/ic_launcher.png"));
                            Bitmap bitmap = base64ToBitmap(facephoto);
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (bitmap != null) {
                                Log.e("mHnSmzSdkListner","mHnSmzSdkListner===="+mHnSmzSdkListner);
                             boolean result =   mHnSmzSdkListner.getPersonRegister(bitmap, getAddPerson);

                                if (result) {
                                    //注册成功后加载百度数据库
                                    mHnSmzSdkListner.loadFacesFromDB();
                                }
//
                            }

                            Log.e("Register","Register注册成功="+facephoto);
                        } else {
                            Log.e("getAddPersonRegister","###用户id =" +getAddPerson.getUser_id() + " 用户名:" + getAddPerson.getName() + "没有照片");
                        }
                    }
                }
//                if (mHnSmzSdkListner != null) {//重新加载百度数据库
//                    mHnSmzSdkListner.loadFacesFromDB();
//                }
            }
        });
    }
    public static void logutil(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.e(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.e(tag, msg);
    }

}
