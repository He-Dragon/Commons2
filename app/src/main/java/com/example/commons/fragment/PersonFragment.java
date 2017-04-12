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

    /**
     * 用getSupportFragmentManager().beginTransaction()方法的show和hind方法第一次创建frgment的时候走生命周期
     * ，之后都不会走生命周期，用frgment的onHiddenChanged方法来控制，如果已经加载到FragmentTransaction后，
     * 页面已经处于可见状态的时候，hidden为true
     * */
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
