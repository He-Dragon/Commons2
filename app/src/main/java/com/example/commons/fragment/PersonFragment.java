package com.example.commons.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commons.R;
import com.example.commons.base.BaseFragment;

/**
 * Created by 阿龙 on 2017/2/27.
 */

public class PersonFragment extends BaseFragment {

    public static PersonFragment newInstance(){
        Bundle args = new Bundle();
        PersonFragment homeFragment = new PersonFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public int getContentView() {
        return R.layout.fagment_person;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void procssClick(View v) {

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            Log.d(TAG, "onHiddenChanged: 123");
        }else {
            Log.d(TAG, "onHiddenChanged: 234");
        }
    }

}
