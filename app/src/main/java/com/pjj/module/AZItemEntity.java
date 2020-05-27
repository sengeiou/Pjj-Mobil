package com.pjj.module;


public class AZItemEntity<T> {

    private T mValue;
    private String mSortLetters;
    private int index=-1;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getValue() {
        return mValue;
    }

    public void setValue(T value) {
        mValue = value;
    }

    public String getSortLetters() {
        return mSortLetters;
    }

    public void setSortLetters(String sortLetters) {
        mSortLetters = sortLetters;
    }
}
