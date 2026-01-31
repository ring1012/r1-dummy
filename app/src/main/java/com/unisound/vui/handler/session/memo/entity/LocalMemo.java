package com.unisound.vui.handler.session.memo.entity;

import android.content.ContentUris;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.vui.handler.session.memo.utils.MemoConstants;
import com.unisound.vui.handler.session.memo.utils.RingingUtils;
import com.unisound.vui.util.TimeUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/* loaded from: classes.dex */
public class LocalMemo extends DataSupport implements Comparable<LocalMemo> {
    private int countDown;
    private int executeCount;
    private long id;
    private boolean isEnabled;
    private boolean isLocalCreateUpDdate;
    private boolean isRepeat;
    private boolean isSnoozed;
    private String memoContent;
    private String memoCreateTime;
    private int memoDay;
    private int memoHour;
    private String memoId;
    private int memoMinute;
    private int memoMonth;
    private int memoSecond;
    private String memoTone;
    private String memoType;
    private int memoYear;
    private String repeatType;

    @Column(defaultValue = RingingUtils.RINGING_DEFAULT)
    private String ringing;
    private int snoozeHour;
    private int snoozeMinute;
    private int snoozeSeconds;
    private long timeGap;
    private boolean[] weeks;

    public LocalMemo() {
        this(UUID.randomUUID().toString());
    }

    private LocalMemo(String memoId) {
        this.isLocalCreateUpDdate = true;
        this.memoId = memoId;
        this.weeks = new boolean[]{false, false, false, false, false, false, false};
        this.memoTone = ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, 7L).toString();
        this.isEnabled = true;
        this.memoCreateTime = TimeUtils.getNowDate(MemoConstants.DATE_FORMATE_ONE);
        this.ringing = RingingUtils.RINGING_DEFAULT;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMemoId() {
        return this.memoId;
    }

    public void setMemoId(String memoId) {
        this.memoId = memoId;
    }

    public int getMemoYear() {
        return this.memoYear;
    }

    public void setMemoYear(int memoYear) {
        this.memoYear = memoYear;
    }

    public int getMemoMonth() {
        return this.memoMonth;
    }

    public void setMemoMonth(int memoMonth) {
        this.memoMonth = memoMonth;
    }

    public int getMemoDay() {
        return this.memoDay;
    }

    public void setMemoDay(int memoDay) {
        this.memoDay = memoDay;
    }

    public int getMemoHour() {
        return this.memoHour;
    }

    public void setMemoHour(int memoHour) {
        this.memoHour = memoHour;
    }

    public int getMemoMinute() {
        return this.memoMinute;
    }

    public void setMemoMinute(int memoMinute) {
        this.memoMinute = memoMinute;
    }

    public int getMemoSecond() {
        return this.memoSecond;
    }

    public void setMemoSecond(int memoSecond) {
        this.memoSecond = memoSecond;
    }

    public boolean[] getWeeks() {
        return this.weeks;
    }

    public void setWeeks(boolean[] weeks) {
        this.weeks = weeks;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isRepeat() {
        return !"OFF".equals(this.repeatType);
    }

    public void setRepeat(boolean repeat) {
        this.isRepeat = repeat;
    }

    public String getRepeatType() {
        return this.repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public long getTimeGap() {
        return this.timeGap;
    }

    public void setTimeGap(long timeGap) {
        this.timeGap = timeGap;
    }

    public int getExecuteCount() {
        return this.executeCount;
    }

    public void setExecuteCount(int executeCount) {
        this.executeCount = executeCount;
    }

    public String getMemoType() {
        return this.memoType;
    }

    public void setMemoType(String memoType) {
        this.memoType = memoType;
    }

    public String getMemoContent() {
        return this.memoContent;
    }

    public void setMemoContent(String memoContent) {
        this.memoContent = memoContent;
    }

    public String getMemoTone() {
        return this.memoTone;
    }

    public void setMemoTone(String memoTone) {
        this.memoTone = memoTone;
    }

    public boolean isSnoozed() {
        return this.isSnoozed;
    }

    public void setSnoozed(boolean snoozed) {
        this.isSnoozed = snoozed;
    }

    public int getSnoozeHour() {
        return this.snoozeHour;
    }

    public void setSnoozeHour(int snoozeHour) {
        this.snoozeHour = snoozeHour;
    }

    public int getSnoozeMinute() {
        return this.snoozeMinute;
    }

    public void setSnoozeMinute(int snoozeMinute) {
        this.snoozeMinute = snoozeMinute;
    }

    public int getSnoozeSeconds() {
        return this.snoozeSeconds;
    }

    public void setSnoozeSeconds(int snoozeSeconds) {
        this.snoozeSeconds = snoozeSeconds;
    }

    public String getMemoCreateTime() {
        return this.memoCreateTime;
    }

    public void setMemoCreateTime(String memoCreateTime) {
        this.memoCreateTime = memoCreateTime;
    }

    public boolean isLocalCreateUpDdate() {
        return this.isLocalCreateUpDdate;
    }

    public void setLocalCreateUpDdate(boolean localCreateUpDdate) {
        this.isLocalCreateUpDdate = localCreateUpDdate;
    }

    public String getRinging() {
        return this.ringing;
    }

    public void setRinging(String ringing) {
        this.ringing = ringing;
    }

    public int getCountDown() {
        return this.countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public boolean isAlarm() {
        return "alarm".equals(this.memoType);
    }

    public boolean isReminder() {
        return "reminder".equals(this.memoType);
    }

    public boolean isCountdown() {
        return MemoConstants.MEMO_TYPE_COUNT_DOWN.equals(this.memoType);
    }

    public void setTime(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        setMemoYear(calendar.get(1));
        setMemoMonth(calendar.get(2) + 1);
        setMemoDay(calendar.get(5));
        setMemoHour(calendar.get(11));
        setMemoMinute(calendar.get(12));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.id).append(PinyinConverter.PINYIN_EXCLUDE);
        builder.append(this.memoId.substring(this.memoId.length() - 12)).append(PinyinConverter.PINYIN_EXCLUDE);
        builder.append(this.memoType).append(PinyinConverter.PINYIN_EXCLUDE);
        builder.append(this.memoYear).append(this.memoMonth).append(this.memoDay).append(PinyinConverter.PINYIN_SEPARATOR);
        builder.append(this.memoHour).append(":").append(this.memoMinute).append(":").append(this.memoSecond).append(PinyinConverter.PINYIN_EXCLUDE);
        builder.append(this.isEnabled).append(PinyinConverter.PINYIN_EXCLUDE);
        builder.append("createTime:").append(this.memoCreateTime).append(PinyinConverter.PINYIN_EXCLUDE);
        builder.append(this.memoContent).append(PinyinConverter.PINYIN_EXCLUDE);
        builder.append(this.repeatType);
        return builder.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalMemo)) {
            return false;
        }
        LocalMemo memo = (LocalMemo) o;
        return getMemoId().equals(memo.getMemoId());
    }

    public int hashCode() {
        return getMemoId().hashCode();
    }

    public long getTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(this.memoYear, this.memoMonth - 1, this.memoDay, this.memoHour, this.memoMinute, this.memoSecond);
        return calendar.getTimeInMillis();
    }

    public String getTimeStr() {
        return TimeUtils.format(getTimeInMillis(), MemoConstants.DATE_FORMATE_ONE);
    }

    private byte getTypeWeight() {
        if ("reminder".equals(this.memoType)) {
            return (byte) 3;
        }
        if (MemoConstants.MEMO_TYPE_COUNT_DOWN.equals(this.memoType)) {
            return (byte) 2;
        }
        return (byte) 1;
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull LocalMemo another) {
        if (getTimeInMillis() != another.getTimeInMillis()) {
            return getTimeInMillis() > another.getTimeInMillis() ? 1 : -1;
        }
        if (getTypeWeight() != another.getTypeWeight()) {
            return getTypeWeight() <= another.getTypeWeight() ? 1 : -1;
        }
        Date date1 = TimeUtils.getDate(this.memoCreateTime, MemoConstants.DATE_FORMATE_ONE);
        Date date2 = TimeUtils.getDate(another.memoCreateTime, MemoConstants.DATE_FORMATE_ONE);
        return date1.getTime() <= date2.getTime() ? -1 : 1;
    }
}
