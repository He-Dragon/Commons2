package com.example.commons.http.subscription;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;

import java.util.Set;

import rx.Subscription;

/**
 * Created by 阿龙 on 2017/4/1.
 * 取消订阅的的管理类（取消网络请求）
 */

public class RxSubscriptionManager implements IRxSubscription<Object> {
    private static RxSubscriptionManager sInstance = null;

    private ArrayMap<Object, Subscription> maps;

    public static RxSubscriptionManager get() {

        if (sInstance == null) {
            synchronized (RxSubscriptionManager.class) {
                if (sInstance == null) {
                    sInstance = new RxSubscriptionManager();
                }
            }
        }
        return sInstance;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private RxSubscriptionManager() {
        maps = new ArrayMap<>();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void add(Object tag, Subscription subscription) {
        maps.put(tag, subscription);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            maps.remove(tag);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void removeAll() {
        if (!maps.isEmpty()) {
            maps.clear();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        //subscription.isUnsubscribed()返回true就是没有订阅
        if (!maps.get(tag).isUnsubscribed()) {
            maps.get(tag).unsubscribe();
            maps.remove(tag);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }
}
