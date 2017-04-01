package com.example.commons.http.subscription;

import rx.Subscription;

/**
 * Created by 阿龙 on 2017/4/1.
 */

public interface IRxSubscription<T> {
    void add(T tag, Subscription subscription);
    void remove(T tag);

    void cancel(T tag);

    void cancelAll();
}
