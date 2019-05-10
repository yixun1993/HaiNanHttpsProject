package com.example.hainanhttpsproject;


import Impl.HnSmzSdkListner;
import JsonBean.*;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.hainanhttpsproject.Dao.DbManger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import util.DesUtil;
import util.HttpMethod;

import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class GetRequest extends AppCompatActivity {

    private static Gson GSON = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HnSmz.getInstance().InitSMZ(this).setSn("56F6-A92A-D7071").setHnSmzSdkListner(new HnSmzSdkListner() {
            @Override
            public void getPersonRegister(Bitmap bitmap, GetAddPerson getAddPerson) {

            }

            @Override
            public void loadFacesFromDB() {
                 //加载百度人脸库
            }

            @Override
            public void cleanAllListner(List<GetAddPerson> getAddPeopleList) {
                  //删除百度人脸库

            }

            @Override
            public void delFaceDataByUserId(String id) {

            }
        }).build();



        //下发
      // getaddPerson();
        // 使用Retrofit封装的方法
//      try {
//        //上传
//            getUpload();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        //删除
      //  getDel();
        //回馈设置返回值
       // getFeedBack();
    }

    /**
     * 人员批量下发http协议
     */
    private final String desKey = "48994686"; //  测试
//    String decryptbyte;//测试人员批量下发数据 和上传数据临时用

    public void getaddPerson() {

     //   Log.e("getRequest","###数据123=="+request);
        //对 发送请求 进行封装
        HttpMethod.getInstance().config().getAddPerson("").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // 步骤7：处理返回的数据结果
                if(response.isSuccessful()){
                    response.body().toString();
                    // Log.e("onResponse","###result=="+response.body().getResult());
                    System.out.println("###数据=="+response.body());

                    try {
                        JSONObject obj = new JSONObject(response.body().toString());
                        String keyContent  =    obj.optString("Content");
                        //判断是否下发成功
                        if(obj.getInt("Result") == 0){
                            Log.e("getAppperson","###getAppperson==="+"下发成功");
                            if(keyContent != null && !("").equals(keyContent)){
                                String decryptbyte =   DesUtil.decrypt( keyContent, Charset.forName("utf-8"),desKey);  //将加密后返回的字节行解密
                                Gson gson1=new Gson();
                                List<GetAddPerson> list= gson1.fromJson(decryptbyte, new TypeToken<List<GetAddPerson>>() {}.getType());
                                Long couI = 1L;
                                for(GetAddPerson addPerson :list){
                                    addPerson.setPid(couI);
                                    //addPerson.
                                    logutil("addPerson","####addPerson===："+ addPerson.getName() + "id=="+addPerson.getId() );
                                    logutil("toString","####addPerson的toString===："+ addPerson.toString());
                                    couI ++ ;
                                    //添加数据库

                                }
                                logutil("GetADD","####DES解密后内容为："+ decryptbyte);
                            }
                        } else {
                            Log.e("getAppperson","###getAppperson==="+"下发失败");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //对数据的处理操作
                }else{
                    Log.e("excption","###异步请求失败出现404");
                    //请求出现错误例如：404 或者 500
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("onFailure","###onFailure连接失败==" +call.getClass());
                Log.e("onFailure","###o请求失败==" +t.toString());
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


    public void getUpload() throws JSONException {


        List<Uploadlogs> uploadlogsList = new ArrayList<>();
         Uploadlogs uploadlogs = new Uploadlogs();
         uploadlogs.setRecog_time("2019-12-12");
         uploadlogs.setRecog_type("iris");
         uploadlogs.setSn("56F6-A92A-D7072");
         uploadlogs.setUser_id("sdsa123131");
         uploadlogsList.add(uploadlogs);
        UploadAttendance uploadAttendance = new UploadAttendance();
        uploadAttendance.setCount(2);
        uploadAttendance.setLogs(uploadlogsList);
        String datajson = GSON.toJson(uploadAttendance);
        Log.e("datajson","###datajson==="+datajson);
        /**
         * 6.3上传识别结果http协议
         */
       String  encrypt =  DesUtil.encrypt(datajson, Charset.forName("utf-8"),desKey);  //进行加密
        Log.e("encrypt","encrypt==="+encrypt);
        HttpMethod.getInstance().config().getUpload("56F6-A92A-D7072",encrypt).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // 步骤7：处理返回的数据结果
                response.body().toString();
                // Log.e("onResponse","###result=="+response.body().getResult());
                System.out.println("###encrypt=="+response.body());
                Log.e("mains","####上传成功");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("onFailure2","###onFailure2连接失败==" +call.getClass());
                Log.e("onFailure2","###o2请求失败==" +t.toString());
            }
        });
    }

    public  void getDel(){
        HttpMethod.getInstance().config().getDel("56F6-A92A-D7071").enqueue(new Callback<Object>() {
            public void onResponse(Call<Object> call, Response<Object> response) {
                // 步骤7：处理返回的数据结果
                    response.body().toString();
                    // Log.e("onResponse","###result=="+response.body().getResult());
                    Log.e("mains","####删除成功");

                    try {
                        JSONObject obj = new JSONObject(response.body().toString());
                        Log.e("mains","####obj=="+obj);
                        String keyContent  =    obj.getString("Content");
                        Log.e("Getrequest","###keyContent=="+keyContent);
                        if (keyContent != null && !keyContent.equals("")){
//                            decryptbyte =   DesUtil.decrypt( keyContent, Charset.forName("utf-8"),desKey);  //将加密后返回的字节行解密
                        }
//                        System.out.println("DES解密后内容为getDel："+ decryptbyte);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("getDel","###getDel2连接失败==" +call.getClass());
                Log.e("getDelonFailure2","###getDel请求失败==" +t.toString());
            }
        });

    }

    public void getFeedBack(){
        FeeBackExample feeBackExample = new FeeBackExample();
        feeBackExample.setType(0);
        feeBackExample.setSn("56F6-A92A-D7072");
        feeBackExample.setMsg("设备回馈成功");
        HttpMethod.getInstance().config().getFeedBack(feeBackExample.getType(),feeBackExample.getSn(),feeBackExample.getMsg()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // 步骤7：处理返回的数据结果
                response.body().toString();
                // Log.e("onResponse","###result=="+response.body().getResult());
                System.out.println("###encrypt=="+response.body());
                Log.e("mains","####回馈成功");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("getFeedBack","###getFeedBack回馈连接失败==" +call.getClass());
                Log.e("getFeedBack","###getFeedBack回馈请求失败==" +t.toString());
            }
        });
    }
}
