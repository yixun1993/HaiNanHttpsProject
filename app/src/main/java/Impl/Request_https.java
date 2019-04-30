package Impl;


import retrofit2.Call;
import retrofit2.http.*;


public interface Request_https {


     @GET("GetAddPerson")
     Call<Object> getAddPerson(@Query("sn") String sn);
    // 注解里传入 网络请求 的部分URL地址
    // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
    // 如果接口里的ur l是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
    // getCall()是接受网络请求数据的方法
    //@FormUrlEncoded
     @POST("UploadAttendance")//?sn=56F6-A92A-D7072
     @FormUrlEncoded
     Call<Object> getUpload(@Field("sn") String sn ,@Field("Content") String content);

    @GET("GetDelPerson")
 //   @FormUrlEncoded
    Call<Object> getDel(@Query("sn") String sn);

    @POST("FeedBack")
    @FormUrlEncoded
    Call<Object> getFeedBack(@Field("type") int type,@Field("sn") String sn,@Field("msg") String msg);

}
