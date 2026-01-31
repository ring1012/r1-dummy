package com.unisound.vui.handler.session.memo.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.ant.device.bean.AlarmReminder;
import com.unisound.vui.handler.session.memo.entity.LocalMemo;
import com.unisound.vui.handler.session.memo.schulding.MemoDataManager;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.TimeUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import nluparser.scheme.AlarmIntent;
import nluparser.scheme.ReminderIntent;

/* loaded from: classes.dex */
public final class MemoUtils {
    private static final String TAG = "memolog-MemoUtils";

    private MemoUtils() {
    }

    public static LocalMemo generateCountdownMemo(@NonNull AlarmIntent alarmIntent) {
        LocalMemo localMemo = new LocalMemo();
        Calendar calendar = Calendar.getInstance();
        calendar.add(13, alarmIntent.getCountDown() / 1000);
        localMemo.setMemoYear(calendar.get(1));
        localMemo.setMemoMonth(calendar.get(2) + 1);
        localMemo.setMemoDay(calendar.get(5));
        localMemo.setMemoHour(calendar.get(11));
        localMemo.setMemoMinute(calendar.get(12));
        localMemo.setMemoSecond(calendar.get(13));
        localMemo.setRepeat(false);
        localMemo.setRepeatType(alarmIntent.getRepeatDate());
        localMemo.setMemoContent(alarmIntent.getLabel());
        localMemo.setMemoType(MemoConstants.MEMO_TYPE_COUNT_DOWN);
        localMemo.setCountDown(alarmIntent.getCountDown());
        localMemo.setEnabled(true);
        localMemo.setRinging(MemoDataManager.getInstance().getFavoriteRingtone());
        return localMemo;
    }

    public static LocalMemo generateAlarmMemo(AlarmIntent alarmIntent) {
        Calendar calendar;
        String startTime = alarmIntent.getDate() + PinyinConverter.PINYIN_SEPARATOR + alarmIntent.getTime();
        LogMgr.d(TAG, "generateAlarmMemo, " + startTime);
        Date memoDate = TimeUtils.getDate(startTime, MemoConstants.DATE_FORMATE_TWO);
        if (memoDate.getTime() < System.currentTimeMillis() + 1000 && "OFF".equals(alarmIntent.getRepeatDate())) {
            return null;
        }
        if ("OFF".equals(alarmIntent.getRepeatDate())) {
            calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTime(memoDate);
        } else {
            String time = alarmIntent.getTime();
            if (time.length() > 5) {
                time = time.substring(0, time.length() - 3);
            }
            calendar = generateDateForRepeatMemo(alarmIntent.getRepeatDate(), time);
        }
        LocalMemo localMemo = new LocalMemo();
        localMemo.setMemoYear(calendar.get(1));
        localMemo.setMemoMonth(calendar.get(2) + 1);
        localMemo.setMemoDay(calendar.get(5));
        localMemo.setMemoHour(calendar.get(11));
        localMemo.setMemoMinute(calendar.get(12));
        localMemo.setMemoSecond(calendar.get(13));
        if (!"OFF".equals(alarmIntent.getRepeatDate())) {
            localMemo.setRepeat(true);
        }
        localMemo.setRepeatType(alarmIntent.getRepeatDate());
        localMemo.setMemoContent(alarmIntent.getLabel());
        localMemo.setMemoType("alarm");
        localMemo.setEnabled(true);
        localMemo.setRinging(MemoDataManager.getInstance().getFavoriteRingtone());
        return localMemo;
    }

    public static LocalMemo generateReminderMemo(ReminderIntent reminderIntent) {
        LocalMemo localMemo = new LocalMemo();
        Date memoDate = TimeUtils.getDate(reminderIntent.getDateTime(), MemoConstants.DATE_FORMATE_TWO);
        if (System.currentTimeMillis() >= memoDate.getTime() && "OFF".equals(reminderIntent.getRepeatType())) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(memoDate);
        localMemo.setMemoYear(calendar.get(1));
        localMemo.setMemoMonth(calendar.get(2) + 1);
        localMemo.setMemoDay(calendar.get(5));
        localMemo.setMemoHour(calendar.get(11));
        localMemo.setMemoMinute(calendar.get(12));
        localMemo.setMemoSecond(calendar.get(13));
        localMemo.setRepeat(true);
        localMemo.setRepeatType(reminderIntent.getRepeatType());
        localMemo.setMemoContent(reminderIntent.getContent());
        localMemo.setMemoType("reminder");
        localMemo.setEnabled(true);
        localMemo.setRinging(MemoDataManager.getInstance().getFavoriteRingtone());
        return localMemo;
    }

    public static String getSetMemoNluTime(LocalMemo memo, String[] nluTimeDay, String[] nluTimeDuration) {
        String tempDuration;
        StringBuilder nluTimeBuilder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        String repeatDate = memo.getRepeatType();
        if (!memo.isRepeat()) {
            if (calendar.get(1) == memo.getMemoYear() && calendar.get(2) + 1 == memo.getMemoMonth()) {
                if (calendar.get(5) == memo.getMemoDay()) {
                    nluTimeBuilder.append(nluTimeDay[0]);
                } else if (memo.getMemoDay() - calendar.get(5) == 1) {
                    nluTimeBuilder.append(nluTimeDay[1]);
                } else if (memo.getMemoDay() - calendar.get(5) == 2) {
                    nluTimeBuilder.append(nluTimeDay[2]);
                } else {
                    nluTimeBuilder.append(String.format(nluTimeDay[3], Integer.valueOf(memo.getMemoMonth()), Integer.valueOf(memo.getMemoDay()), Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                }
            } else {
                nluTimeBuilder.append(String.format(nluTimeDay[3], Integer.valueOf(memo.getMemoMonth()), Integer.valueOf(memo.getMemoDay()), Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
            }
            if (memo.getMemoDay() - calendar.get(5) >= 0 && memo.getMemoDay() - calendar.get(5) < 3) {
                if (memo.getMemoHour() < 8) {
                    tempDuration = nluTimeDuration[0];
                } else if (memo.getMemoHour() < 11) {
                    tempDuration = nluTimeDuration[1];
                } else if (memo.getMemoHour() < 13) {
                    tempDuration = nluTimeDuration[2];
                } else if (memo.getMemoHour() < 19) {
                    tempDuration = nluTimeDuration[3];
                } else {
                    tempDuration = nluTimeDuration[4];
                }
                nluTimeBuilder.append(String.format(tempDuration, Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
            }
        } else {
            try {
                if ("YEAR".equals(repeatDate)) {
                    nluTimeBuilder.append(String.format(nluTimeDay[4], Integer.valueOf(memo.getMemoMonth()), Integer.valueOf(memo.getMemoDay()), Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                } else if ("MONTH".equals(repeatDate)) {
                    nluTimeBuilder.append(String.format(nluTimeDay[5], Integer.valueOf(memo.getMemoDay()), Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                } else if ("DAY".equals(repeatDate)) {
                    nluTimeBuilder.append(String.format(nluTimeDay[6], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                } else if ("WORKDAY".equals(repeatDate)) {
                    nluTimeBuilder.append(String.format(nluTimeDay[7], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                } else if ("WEEKEND".equals(repeatDate)) {
                    nluTimeBuilder.append(String.format(nluTimeDay[8], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                } else if (repeatDate != null) {
                    new StringBuilder();
                    if (repeatDate.contains("D1")) {
                        nluTimeBuilder.append(String.format(nluTimeDay[9], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                    } else if (repeatDate.contains("D2")) {
                        nluTimeBuilder.append(String.format(nluTimeDay[10], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                    } else if (repeatDate.contains("D3")) {
                        nluTimeBuilder.append(String.format(nluTimeDay[11], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                    } else if (repeatDate.contains("D4")) {
                        nluTimeBuilder.append(String.format(nluTimeDay[12], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                    } else if (repeatDate.contains("D5")) {
                        nluTimeBuilder.append(String.format(nluTimeDay[13], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                    } else if (repeatDate.contains("D6")) {
                        nluTimeBuilder.append(String.format(nluTimeDay[14], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                    } else if (repeatDate.contains("D7")) {
                        nluTimeBuilder.append(String.format(nluTimeDay[15], Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute())));
                    }
                }
            } catch (NumberFormatException e) {
                LogMgr.e(TAG, "NumberFormatException e:" + e);
            }
        }
        return nluTimeBuilder.toString();
    }

    public static AlarmReminder getAlarmReminder(String status, LocalMemo memo) {
        AlarmReminder alarmReminder = new AlarmReminder();
        alarmReminder.setId(memo.getMemoId());
        alarmReminder.setOpen(memo.isEnabled());
        alarmReminder.setRepeatDate(memo.getRepeatType());
        alarmReminder.setDate(String.format(Locale.CHINA, "%04d-%02d-%02d", Integer.valueOf(memo.getMemoYear()), Integer.valueOf(memo.getMemoMonth()), Integer.valueOf(memo.getMemoDay())));
        String mTime = String.format(Locale.CHINA, "%02d:%02d:%02d", Integer.valueOf(memo.getMemoHour()), Integer.valueOf(memo.getMemoMinute()), Integer.valueOf(memo.getMemoSecond()));
        alarmReminder.setTime(mTime);
        alarmReminder.setLabel(memo.getMemoContent());
        alarmReminder.setStatus(status);
        alarmReminder.setType(memo.getMemoType());
        alarmReminder.setRinging(memo.getRinging());
        alarmReminder.setCountDown(memo.getCountDown());
        return alarmReminder;
    }

    public static LocalMemo getLocalMemo(AlarmReminder alarmReminder) {
        Date dateTime;
        LocalMemo localMemo = new LocalMemo();
        localMemo.setMemoId(alarmReminder.getId());
        localMemo.setEnabled(alarmReminder.isOpen());
        localMemo.setRepeatType(alarmReminder.getRepeatDate());
        localMemo.setRepeat("OFF".equals(alarmReminder.getRepeatDate()) ? false : true);
        localMemo.setMemoContent(alarmReminder.getLabel());
        localMemo.setMemoType(alarmReminder.getType());
        localMemo.setRinging(alarmReminder.getRinging());
        localMemo.setCountDown(alarmReminder.getCountDown());
        try {
            if (TextUtils.isEmpty(alarmReminder.getDate()) || TextUtils.isEmpty(alarmReminder.getTime())) {
                LogMgr.d(TAG, "getLocalMemo memoDay is null or memoTime is null");
                return null;
            }
            String repeatType = alarmReminder.getRepeatDate();
            if ("alarm".equals(alarmReminder.getType()) && !TextUtils.isEmpty(repeatType) && !"OFF".equals(repeatType)) {
                String timeStr = alarmReminder.getTime();
                Calendar calendar = generateDateForRepeatMemo(repeatType, timeStr);
                localMemo.setMemoYear(calendar.get(1));
                localMemo.setMemoMonth(calendar.get(2) + 1);
                localMemo.setMemoDay(calendar.get(5));
            } else {
                Date date = TimeUtils.getDate(alarmReminder.getDate(), MemoConstants.DATE_FORMATE_YMD);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date);
                localMemo.setMemoYear(calendar2.get(1));
                localMemo.setMemoMonth(calendar2.get(2) + 1);
                localMemo.setMemoDay(calendar2.get(5));
            }
            if (MemoConstants.MEMO_TYPE_COUNT_DOWN.equals(localMemo.getMemoType())) {
                dateTime = TimeUtils.getDate(alarmReminder.getTime(), MemoConstants.DATE_FORMATE_HMS);
            } else {
                dateTime = TimeUtils.getDate(alarmReminder.getTime(), MemoConstants.DATE_FORMATE_HM);
            }
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTime(dateTime);
            localMemo.setMemoHour(calendarTime.get(11));
            localMemo.setMemoMinute(calendarTime.get(12));
            localMemo.setMemoSecond(calendarTime.get(13));
            return localMemo;
        } catch (Exception e) {
            e.printStackTrace();
            return localMemo;
        }
    }

    @NonNull
    private static Calendar generateDateForRepeatMemo(String repeatType, String timeStr) {
        Calendar calendar;
        int dayOfWeekNow;
        Date time = TimeUtils.getDate(timeStr, MemoConstants.DATE_FORMATE_HM);
        calendar = Calendar.getInstance();
        calendar.set(11, time.getHours());
        calendar.set(12, time.getMinutes());
        calendar.set(13, 0);
        calendar.set(14, 0);
        dayOfWeekNow = calendar.get(7);
        switch (repeatType) {
            case "DAY":
                if (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    calendar.add(5, 1);
                }
                return calendar;
            case "WORKDAY":
                if (dayOfWeekNow == 6) {
                    if (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                        calendar.add(5, 3);
                    }
                } else if (dayOfWeekNow == 7) {
                    calendar.add(5, 2);
                } else if (dayOfWeekNow == 1 || calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    calendar.add(5, 1);
                }
                return calendar;
            case "WEEKEND":
                if (dayOfWeekNow == 7) {
                    if (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                        calendar.add(5, 1);
                    }
                } else if (dayOfWeekNow != 1) {
                    int increment = 7 - dayOfWeekNow;
                    calendar.add(5, increment);
                } else if (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    calendar.add(5, 6);
                }
                return calendar;
            default:
                String[] repeatDays = repeatType.split(",");
                List<Integer> repeatDayInts = MemoConstants.mapRepeatDaysToInts(repeatDays);
                if (!repeatDayInts.contains(Integer.valueOf(dayOfWeekNow)) || calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    int increment2 = 0;
                    Iterator<Integer> it = repeatDayInts.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            int day = it.next().intValue();
                            if (dayOfWeekNow < day) {
                                increment2 = day - dayOfWeekNow;
                            }
                        }
                    }
                    if (increment2 == 0) {
                        increment2 = (repeatDayInts.get(0).intValue() - dayOfWeekNow) + 7;
                    }
                    calendar.add(5, increment2);
                }
                return calendar;
        }
    }

    public static void calculateNextTime(LocalMemo memo) {
        if (memo.isAlarm() && !"OFF".equals(memo.getRepeatType())) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTimeInMillis(memo.getTimeInMillis());
            if (memo.getRepeatType().equals("DAY")) {
                while (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    calendar.add(5, 1);
                }
            } else if (memo.getRepeatType().equals("MONTH")) {
                while (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    calendar.add(2, 1);
                }
            } else if (memo.getRepeatType().equals("YEAR")) {
                while (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    calendar.add(1, 1);
                }
            } else if (memo.getRepeatType().equals("WORKDAY")) {
                while (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    if (calendar.get(7) == 6) {
                        calendar.add(5, 3);
                    } else if (calendar.get(7) == 7) {
                        calendar.add(5, 2);
                    } else {
                        calendar.add(5, 1);
                    }
                }
            } else if (memo.getRepeatType().equals("WEEKEND")) {
                while (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    if (calendar.get(7) == 7) {
                        calendar.add(5, 1);
                    } else if (calendar.get(7) == 1) {
                        calendar.add(5, 6);
                    } else {
                        int increment = 7 - calendar.get(7);
                        calendar.add(5, increment);
                    }
                }
            } else {
                String[] repeatDays = memo.getRepeatType().split(",");
                List<Integer> repeatDayInts = MemoConstants.mapRepeatDaysToInts(repeatDays);
                while (calendar.getTimeInMillis() < System.currentTimeMillis() + 1000) {
                    int increment2 = 0;
                    int nowDayOfWeek = calendar.get(7);
                    int index = repeatDayInts.indexOf(Integer.valueOf(nowDayOfWeek));
                    if (index < 0) {
                        Iterator<Integer> it = repeatDayInts.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            int day = it.next().intValue();
                            if (nowDayOfWeek < day) {
                                increment2 = day - nowDayOfWeek;
                                break;
                            }
                        }
                        if (increment2 == 0) {
                            increment2 = (repeatDayInts.get(0).intValue() - nowDayOfWeek) + 7;
                        }
                    } else if (index < repeatDayInts.size() - 1) {
                        increment2 = repeatDayInts.get(index + 1).intValue() - nowDayOfWeek;
                    } else {
                        increment2 = (repeatDayInts.get(0).intValue() - nowDayOfWeek) + 7;
                    }
                    calendar.add(5, increment2);
                }
            }
            memo.setTime(calendar.getTimeInMillis());
            LogMgr.d(TAG, "calculateNextTime, result:" + memo.getTimeStr());
        }
    }
}
