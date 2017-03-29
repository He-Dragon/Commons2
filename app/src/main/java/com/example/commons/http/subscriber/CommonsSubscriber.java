package com.example.commons.http.subscriber;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.commons.utils.CustomDialog;

import rx.Subscriber;

/**
 * Created by 阿龙 on 2017/3/29.
 */

public abstract class CommonsSubscriber<T> extends Subscriber<T> {

    private Context mContext;
    private boolean isLoading = false;

    //带加载loding的构造器
    public CommonsSubscriber(Context mContext, boolean isLoading){
        this.mContext = mContext;
        this.isLoading = isLoading;
    }
    //不用加载框的构造器
    public CommonsSubscriber(){
//        this.mContext = mContext;
    }
    @Override
    public void onStart() {
        if (isLoading){
            CustomDialog.createLodingDialog(mContext,"加载中...").show();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        CustomDialog.cancelLodingDialog();//取消加载框
        Log.d(">>>>>>", "onError: "+e.getLocalizedMessage());
        onError(e.getLocalizedMessage());
    }

    @Override
    public void onNext(T t) {
        CustomDialog.cancelLodingDialog();//取消加载框
        onSuccess(t);
    }



    public abstract void onSuccess(T t);

    public abstract void onError(String errorMsg);

}
