package com.pjj.observemodel;

import java.util.HashMap;

/**
 * Create by xinheng on 2018/10/16。
 * describe：管理观察者模式
 * 简单应用，不考虑跨线程，只是主线程
 */
public class MyBus {
    private static MyBus instant;
    public static MyBus getInstant() {
        if(null==instant){
            synchronized (MyBus.class){
                if(null==instant){
                    instant=new MyBus();
                }
            }
        }
        return instant;
    }
    private HashMap<String,Observer> observerMap;
    private HashMap<String,Observed> observedMap;
    private MyBus(){
        observerMap=new HashMap<>();
        observedMap=new HashMap<>();
    }

    public <T> void register(Object object,Observer<T> observer){
        observerMap.put(getClassName(object),observer);
    }
    public void unregister(Object object){
        observerMap.remove(getClassName(object));
    }

    public <T> void postMessage(T t,Class<?> aClass){
        String key = aClass.getName();
        Observed<T> observed;
        if(observedMap.containsKey(key)){
            observed = observedMap.get(key);
        }else {
            observed=new Observed<>();
            observedMap.put(key,observed);
        }
        if(observerMap.containsKey(key)){
            Observer observer = observerMap.get(key);
            if(null!=observer) {
                observed.subscribe(observer);
            }
        }
        observed.sendMessage(t);
    }
    private String getClassName(Object object){
        return object.getClass().getName();
    }
}
