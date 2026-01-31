package com.unisound.vui.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class TimeUtils {
    private static final String TAG = TimeUtils.class.getSimpleName();

    public static String format(long time, String format) {
        return new SimpleDateFormat(format, Locale.CHINA).format(new Date(time));
    }

    public static String format(String str, String oldFormat, String newFormat) {
        return format(new SimpleDateFormat(oldFormat, Locale.CHINA).parse(str).getTime(), newFormat);
    }

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }

    public static long getCurrentTime() throws IOException {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long unisoundWebTime = getUnisoundWebTime();
        LogMgr.d(TAG, "getUnisoundWebTime = " + unisoundWebTime);
        if (unisoundWebTime <= 0) {
            unisoundWebTime = getWebsiteDatetime("http://www.baidu.com");
            LogMgr.d(TAG, "getBaiduWebTime = " + unisoundWebTime);
        }
        return (unisoundWebTime <= 0 || Math.abs(jCurrentTimeMillis - unisoundWebTime) <= 300000) ? jCurrentTimeMillis : unisoundWebTime;
    }

    public static Date getDate(String date, String format) {
        return new SimpleDateFormat(format, Locale.CHINA).parse(date);
    }

    public static String getNowDate(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        simpleDateFormat.format(new Date());
        return simpleDateFormat.format(new Date());
    }

    public static long getTime(String date, String format) {
        return new SimpleDateFormat(format, Locale.CHINA).parse(date).getTime();
    }

    private static long getUnisoundWebTime() {
        try {
            HttpUtils.getInstance().cancel("getUnisoundWebTime");
            Response sync = HttpUtils.getInstance().getSync("getUnisoundWebTime", "http://uc.hivoice.cn/timestamp.jsp");
            if (HttpUtils.isResponseCorrect(sync)) {
                return Long.parseLong(sync.body().string().replace(IOUtils.LINE_SEPARATOR_WINDOWS, "").replace("\n", "")) * 1000;
            }
        } catch (Exception e) {
            LogMgr.e(TAG, "getUnisoundWebTime error : ", e);
        }
        return 0L;
    }

    private static long getWebsiteDatetime(String webUrl) throws IOException {
        try {
            URLConnection uRLConnectionOpenConnection = new URL(webUrl).openConnection();
            uRLConnectionOpenConnection.connect();
            return uRLConnectionOpenConnection.getDate();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0L;
        } catch (IOException e2) {
            e2.printStackTrace();
            return 0L;
        }
    }
}
