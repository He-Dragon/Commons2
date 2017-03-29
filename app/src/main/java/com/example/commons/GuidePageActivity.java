package com.example.commons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.commons.base.BasePagerAdapter;
import com.example.commons.utils.SharePrefUtils;

import java.util.ArrayList;

/**
 * Created by hecl on 2016/9/12.
 */
public class GuidePageActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private Button start_Button,jump_Button;
    private BasePagerAdapter mAdapter;
    private ArrayList<View> views;
    private LinearLayout indicator;
    private int[] image = new int[]{R.mipmap.welcome_01, R.mipmap.welcome_02, R.mipmap.welcome_03};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_guidle_page);
        initView();
        createIndex(0);

    }

    private void initView() {
        views = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        indicator = (LinearLayout) findViewById(R.id.indicator);
        start_Button = (Button) findViewById(R.id.start_Button);
        jump_Button = (Button) findViewById(R.id.jump_Button);
        start_Button.setOnClickListener(this);
        jump_Button.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        setData();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.start_Button:
                SharePrefUtils.setBoolean(SharePrefUtils.ISFIRST,true);
                intent.setClass(GuidePageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.jump_Button:
                SharePrefUtils.setBoolean(SharePrefUtils.ISFIRST,true);
                intent.setClass(GuidePageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;

            default:
                break;
        }


    }

    /**
     * 给page设置数据
     */
    private void setData() {
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(image[i]);
            views.add(imageView);
        }
        mAdapter = new BasePagerAdapter(views);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        if (position == image.length - 1) {
            start_Button.setVisibility(View.VISIBLE);
            jump_Button.setVisibility(View.INVISIBLE);
        } else {
            start_Button.setVisibility(View.INVISIBLE);
            jump_Button.setVisibility(View.VISIBLE);

        }
        createIndex(position);
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }


    public void createIndex(int position){
        indicator.removeAllViews();
        for (int i = 0; i < 3; i++) {
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i == position) {
                image.setImageResource(R.drawable.circle_dot);
            }else {
                image.setImageResource(R.drawable.circle_dot_side);
            }

            if (i == 0) {
                params.setMargins(30, 0, 30, 0);
            }else {
                params.setMargins(0, 0, 30, 0);
            }
            params.height = 30;
            params.width = 30;
            image.setLayoutParams(params);
            indicator.addView(image);
        }
    }

}
