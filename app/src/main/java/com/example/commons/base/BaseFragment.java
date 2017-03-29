package com.example.commons.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by 阿龙 on 2017/2/28.
 */

public abstract class BaseFragment extends Fragment {

    private View view ;
    private LayoutInflater inflater;
    private  ViewGroup container ;

    protected Toast xToast;
    public static final String TAG = ">>>>>>>>>>>>>";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater ;
        this.container = container ;
        return super.onCreateView(inflater, container, savedInstanceState);

    }



    public View setContentView(int resourceId) {
        view = inflater.inflate(resourceId, container, false);
        return view;
    }
    public View getContentView(){
        return  this.view ;
    }

    /**
     * 初始化View
     */
    public abstract void initView();

    /**
     * 给控件设置数据
     */
    public abstract void setViewData();
    /**
     * 加载数据
     */
    public abstract void loadData();


    /***
     * 显示Toast
     */
    public void showXToast(String msg) {
        if (null != xToast) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                xToast.cancel();
            }
        } else {
            xToast = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        xToast.show();
        xToast.setText(msg);
    }


    /**
     * 获取控件id
     * @param id
     * @return
     */
    public View findViewById(int id){
        return view.findViewById(id) ;
    }
}
