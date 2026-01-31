package com.unisound.vui.handler.filter;

import java.util.ArrayList;
import java.util.List;
import nluparser.scheme.SName;

/**
 * 判断是不是云之声服务，返回结果
 */
public class YzsServiceUtils {

    /* renamed from: a  reason: collision with root package name */
    private static List<String> serverList = new ArrayList();

    static {
        serverList.add(SName.SETTING_MP);
        serverList.add(SName.CHAT);
        serverList.add(SName.ERROR_REPORT);
    }

    /**
     * 检查是不是云之声的服务返回结果
     * @param str
     * @return
     */
    public static boolean checkService(String str) {
        return serverList.contains(str);
    }
}
