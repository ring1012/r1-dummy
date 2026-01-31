package com.unisound.vui.handler.session.memo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class MemoConstants {
    public static final String ACTION_MEMO_RING = "cn.yunzhisheng.action.ring";
    public static final String ACTION_MEMO_RING_AUDITION = "cn.yunzhisheng.action.ring.audition";
    public static final String D1 = "D1";
    public static final String D2 = "D2";
    public static final String D3 = "D3";
    public static final String D4 = "D4";
    public static final String D5 = "D5";
    public static final String D6 = "D6";
    public static final String D7 = "D7";
    public static final String DATE_FORMATE_HM = "HH:mm";
    public static final String DATE_FORMATE_HMS = "HH:mm:ss";
    public static final String DATE_FORMATE_ONE = "yyyyMMdd HH:mm:ss";
    public static final String DATE_FORMATE_TWO = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMATE_YMD = "yyyy-MM-dd";
    public static final String DATE_FORMATE_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DAY = "DAY";
    public static final String MEMO_TAG = "memolog-";
    public static final String MEMO_TYPE_ALARM = "alarm";
    public static final String MEMO_TYPE_COUNT_DOWN = "countDown";
    public static final String MEMO_TYPE_REMINDER = "reminder";
    public static final String MONTH = "MONTH";
    public static final String OFF = "OFF";
    public static final int VALID_TIME_OFFSET_IN_MILLIS = 3000;
    public static final String WEEKEND = "WEEKEND";
    public static final String WORKDAY = "WORKDAY";
    public static final String YEAR = "YEAR";

    public static List<Integer> mapRepeatDaysToInts(String[] values) {
        List<Integer> weekList = new ArrayList<>();
        for (String week : values) {
            switch (week) {
                case "D1":
                    weekList.add(2);
                    break;
                case "D2":
                    weekList.add(3);
                    break;
                case "D3":
                    weekList.add(4);
                    break;
                case "D4":
                    weekList.add(5);
                    break;
                case "D5":
                    weekList.add(6);
                    break;
                case "D6":
                    weekList.add(7);
                    break;
                case "D7":
                    weekList.add(1);
                    break;
            }
        }
        Collections.sort(weekList);
        return weekList;
    }

    private MemoConstants() {
    }
}
