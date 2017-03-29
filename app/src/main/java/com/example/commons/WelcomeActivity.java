package com.example.commons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.commons.utils.SharePrefUtils;


/**
 * Created by hecl on 2016/9/12.
 */
public class WelcomeActivity extends Activity {

    private boolean isFirst,isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = SharePrefUtils.getBoolean(SharePrefUtils.ISFIRST,false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                if (isFirst) {
                    intent.setClass(WelcomeActivity.this,MainActivity.class);
                }else {
                    intent.setClass(WelcomeActivity.this,GuidePageActivity.class);
                }
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        },2000);
    }
}
