package com.example.commons.http;

/**
 * Created by 阿龙 on 2017/3/3.
 */

public class RxRetrofitUtil {

    public RxRetrofitUtil() {


    }
    private static class SingletonHolder{
        private static final RxRetrofitUtil INSTANCE = new RxRetrofitUtil();
    }

    public static RxRetrofitUtil getInstence() {
        return SingletonHolder.INSTANCE;
    }



    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
//    public <T> T create(Class<T> service){
////        return RxRetrofitClient.getInstence().create(service);
//    }
}
