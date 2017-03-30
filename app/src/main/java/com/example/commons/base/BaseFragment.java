package com.example.commons.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by 阿龙 on 2017/2/28.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private View view;
    private SparseArray<View> mViews;

    protected Toast xToast;
    public static final String TAG = ">>>>>>>>>>>>>";


    @Override
    public void onClick(View v) {
        procssClick(v);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            mViews = new SparseArray<>();
            view = inflater.inflate(getContentView(), container, false);
            initView();
            initData();
            initListener();
        }
        return view;

    }

    public abstract int getContentView();

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
     * 获取控件id
     *
     * @param id
     * @return
     */
    public View findViewById(int id) {
        return view.findViewById(id);
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
            xToast = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        xToast.show();
        xToast.setText(msg);
    }


}
