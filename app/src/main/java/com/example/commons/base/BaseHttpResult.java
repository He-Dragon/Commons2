package com.example.commons.base;

/**
 * Created by 阿龙 on 2017/3/28.
 * 网络请求返回参数与后台约定的固定参数
 */

public class BaseHttpResult<T> {
    public int code;
    public int meg;
    public T data;
}
