package com.chenandroid.sortlist;

import com.chenandroid.UnLockApp;

import java.util.Comparator;

/**
 * @Description:拼音的比较器
 * @author http://blog.csdn.net/finddreams
 */ 
public class PinyinunComparator implements Comparator<UnLockApp> {

	public int compare(UnLockApp o1, UnLockApp o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
