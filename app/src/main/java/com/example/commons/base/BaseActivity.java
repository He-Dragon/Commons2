package com.example.commons.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.commons.http.RxRetrofitClient;
import com.example.commons.http.service.HttpService;
import com.example.commons.http.subscriber.CommonsSubscriber;
import com.example.commons.http.subscriber.RxHelper;
import com.example.commons.http.subscription.RxSubscriptionManager;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;


/**
 * Created by 阿龙 on 2017/2/15.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Toast xToast;
    protected Context mContext;
    private static View decorView = null;
    public static final String TAG = ">>>>>>>>>>>>>";
    private SparseArray<View> mViews;


    @Override
    public void onClick(View v) {
        procssClick(v);
    }
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mViews = new SparseArray<>();
        setContentView(getContentViewId());
        setStatusBar(this);
        mContext = this;
        initView();
        initData();
        initListener();
//        /**
//         * 网络请求实列
//         * */
//        Map<String, String> map = new HashMap<>();
//        map.put("key", "valus");
//         Subscription subscription = RxRetrofitClient.getInstence()
//                .create(HttpService.class)
//                .login(map)
//                .compose(RxHelper.<BaseHttpResult>io_main())
//                .subscribe(new CommonsSubscriber<BaseHttpResult>() {
//                    @Override
//                    public void onSuccess(BaseHttpResult baseHttpResult) {
//
//                    }
//
//                    @Override
//                    public void onError(String errorMsg) {
//
//                    }
//                });
//
//        RxSubscriptionManager.get().add("key",subscription);//添加，在请求时候添加
//        RxSubscriptionManager.get().remove("key");//取消订阅，一般在onDestroy中取消

    }

    /**
     * 初始化View
     */
    public abstract void initView();

    /**
     * 给控件设置数据
     */
    public abstract void initData();

    /**
     * 设置监听
     */
    public abstract void initListener();
    /**
     * 点击事件点击回调
     */
    public abstract void procssClick(View v);
    /**
     * 布局
     */
    public abstract int getContentViewId();

    /**
     * 通过ID找到view
     */
    public <E extends View> E F(int viewId) {
        E view = (E) mViews.get(viewId);
        if (view == null){
            view = (E) findViewById(viewId);
            mViews.put(viewId,view);
        }
        return view;
    }
    /**
     * 通过view设置点击监听
     */
    public <E extends View> void C(E view){
        view.setOnClickListener(this);
    }


    /***
     * 显示Toast
     */
    public void showXToast(String msg) {
        if (null != xToast) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                xToast.cancel();
            }
        } else {
            xToast = Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        xToast.show();
        xToast.setText(msg);
    }

    /**
     * 设置沉浸式状态栏
     * 在xml中设置android:fitsSystemWindows="true"属性
     */
    private void setStatusBar(Activity activity) {
        /**
         * 设置页面在状态栏的下面
         * SYSTEM_UI_FLAG_VISIBLE ：状态栏（StatusBar）和虚拟键（StatusBar）都显示，不全屏
         * YSTEM_UI_FLAG_LAYOUT_FULLSCREEN //页面到最顶部，状态栏不会隐藏，页面处于状态栏（StatusBar）的下面
         *
         * */
        decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        /**
         * setStatusBarColor这个方法只有在5.0以上的版本在会有。
         * 在5.0以上的版本的状态栏StatusBar有颜色的，所以用setStatusBarColor设置状态栏的颜色
         * */
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window statusBar = getWindow();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            statusBar.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            statusBar.setStatusBarColor(Color.TRANSPARENT);//状态栏（StatusBar）背景设置成透明色
        } else if (android.os.Build.VERSION.SDK_INT >= 14) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏（StatusBar）
        } else {
            //在4.0以下就没有setSystemUiVisibility这个方法（隐藏状态栏（StatusBar））
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
