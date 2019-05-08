package util;

import Impl.Request_https;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class HttpMethod {
    //在自己内部定义自己的一个实例，只供内部调用
    private static final HttpMethod instance = new HttpMethod();
    private HttpMethod(){
    }
    //这里提供了一个供外部访问本class的静态方法，可以直接访问
    public static HttpMethod getInstance(){
        return instance;
    }

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印retrofit日志
            try {
                String text = URLDecoder.decode(message, "utf-8");
                Log.e( "lanj","####api_msg:" + text);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    });
    public static void logutil(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.d(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }

    public Request_https config(){
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client  = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) //添加拦截器
                //  .addInterceptor(new CommonInterceptor(sendBo)) //注入header
                .build();
        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://36.101.208.177:7106/Web/Service/DevivePacketWebSvr.assx/") // 设置 测试地址 Url
                .baseUrl("http://sys.hizj.net:8106/Service/DevivePacketWebSvr.assx/") // 设置 正式地址 Url
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        // 步骤5:创建 网络请求接口 的实例
        Request_https request = retrofit.create(Request_https.class);
        return request;
    }


}
