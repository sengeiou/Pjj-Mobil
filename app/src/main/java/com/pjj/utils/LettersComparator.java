package com.pjj.utils;

import com.pjj.module.AZItemEntity;

import java.util.Comparator;

/**
 * Created by XinHeng on 2018/12/03.
 * describe：字母排序
 */
public class LettersComparator implements Comparator<AZItemEntity> {

    public int compare(AZItemEntity o1, AZItemEntity o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return 1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return -1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
