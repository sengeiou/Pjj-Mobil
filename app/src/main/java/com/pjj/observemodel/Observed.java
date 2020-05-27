package com.pjj.observemodel;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.pjj.utils.Parameter.checkNull;


/**
 * Create by xinheng on 2018/10/16。
 * describe：被观察者
 */
public class Observed<T> {
    private List<Observer<T>> listObserver=new LinkedList<>();
    public void sendMessage(T t){
        Iterator<Observer<T>> iterator = listObserver.iterator();
        while (iterator.hasNext()){
            Observer<T> observer = iterator.next();
            observer.dealWithMessage(t);
            iterator.remove();
        }
    }
    public synchronized void subscribe(Observer<T> observer){
        checkNull(observer,"the observer is null");
        if(!listObserver.contains(observer)){
            listObserver.add(observer);
        }
    }
    public synchronized void unsubscribe(Observer observer){
        checkNull(observer,"the observer is null");
        listObserver.remove(observer);
    }
}
