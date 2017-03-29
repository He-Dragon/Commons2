package com.example.commons.adapter.base;


import android.view.View;

import com.example.commons.adapter.ViewHolder;


/**
 * Created by hecl on 2016/9/12.
 * 如果一个list里面有多种不同的布局，就实现这个接口，实现不同的View布局
 */
public interface ItemViewDelegate<T>
{

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHolder holder, T t, int position);



}
