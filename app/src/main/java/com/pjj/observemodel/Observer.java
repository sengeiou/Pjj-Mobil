package com.pjj.observemodel;

/**
 * Create by xinheng on 2018/10/16。
 * describe：观察者
 */
public interface Observer<T> {

    void dealWithMessage(T t);
}
