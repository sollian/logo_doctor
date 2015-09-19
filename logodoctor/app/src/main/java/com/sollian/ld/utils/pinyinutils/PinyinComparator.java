package com.sollian.ld.utils.pinyinutils;

import java.util.Comparator;

public class PinyinComparator implements Comparator<IPinYin> {

    public int compare(IPinYin o1, IPinYin o2) {
        if (o1.getSortedLetters().equals("@") || o2.getSortedLetters().equals("#")) {
            return -1;
        } else if (o1.getSortedLetters().equals("#")
            || o2.getSortedLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortedLetters().compareTo(o2.getSortedLetters());
        }
    }

}
