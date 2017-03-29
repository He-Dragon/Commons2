package com.example.commons.http;

import com.example.commons.http.service.HttpService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by 阿龙 on 2017/3/29.
 */

public class RxRetrofitClient {

    private String basUrl = "http://news-at.zhihu.com";
    private Retrofit mRetrofit;
    //设置连接超时的值
    private static final int TIMEOUT = 10;
    //缓存的路劲
    private static File cacheDirectory = new File(com.example.commons.app.Application.getApplication()
            .getCacheDir().getAbsolutePath(), "HttpCache");
    //设置请求的拦截器
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Accept", "*/*")
                    .header("Cache-Control", String.format("public, max-age=%d", 60))
                    .removeHeader("Pragma")
                    .build();
            return chain.proceed(request);
        }
    };

    public static HttpService httpService;

    public RxRetrofitClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                // 设置连接超时
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                //设置从主机读信息超时
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                //设置写信息超时
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
//                    //设置缓存文件
//                    .cache(new Cache(cacheDirectory, 10 * 1024 * 1024))
//                    //设置请求的拦截器
//                    .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(basUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }
    private static class SingletonHolder{
        private static final RxRetrofitClient INSTANCE = new RxRetrofitClient();
    }

    public static RxRetrofitClient getInstence() {
        return SingletonHolder.INSTANCE;
    }



    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }
}
