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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fagment_person);
        initView();
        return getContentView();
    }

    @Override
    public void initView() {

    }

    @Override
    public void setViewData() {

    }

    @Override
    public void loadData() {
        Log.d(TAG, "loadData: PersonFragment");
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
