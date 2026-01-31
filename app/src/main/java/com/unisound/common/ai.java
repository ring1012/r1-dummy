package com.unisound.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class ai {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f239a = true;
    private static final String b = "ResultToJsonUtil";

    public static JSONObject a(InputStream inputStream, float f) throws JSONException, DOMException {
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        JSONArray jSONArray2 = new JSONArray();
        NodeList childNodes = ((Element) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).getChildNodes().item(0)).getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == 1) {
                a(new JSONObject(), jSONArray, new JSONArray(), 0.0f, childNodes.item(i).getNodeName(), childNodes.item(i).getFirstChild().getNodeValue());
            }
        }
        jSONObject.put("l", jSONArray);
        jSONObject.put("score", f);
        jSONArray2.put(jSONObject);
        return new JSONObject().put("c", jSONArray2);
    }

    public static JSONObject a(String str, float f, String str2) throws JSONException {
        a(str2, str);
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        JSONArray jSONArray2 = new JSONArray();
        NodeList childNodes = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(str2)).getChildNodes();
        if (f239a) {
            y.c(b, "FileUtiles -Node.length =", Integer.valueOf(childNodes.getLength()), ", node:value =", childNodes.item(0).getNodeName());
        }
        Element element = (Element) childNodes.item(0);
        NodeList childNodes2 = element.getChildNodes();
        if (f239a) {
            y.c(b, "FileUtiles -childElement =", element.getNodeValue());
        }
        for (int i = 0; i < childNodes2.getLength(); i++) {
            if (childNodes2.item(i).getNodeType() == 1) {
                a(new JSONObject(), jSONArray, new JSONArray(), 0.0f, childNodes2.item(i).getNodeName().replace("_", ""), childNodes2.item(i).getFirstChild().getNodeValue());
            }
        }
        jSONObject.put("l", jSONArray);
        jSONObject.put("score", f);
        jSONArray2.put(jSONObject);
        return new JSONObject().put("c", jSONArray2);
    }

    public static void a(String str, String str2) {
        try {
            String str3 = "<?xml version='1.0' encoding='utf-8'?>" + str2.replace("[", "").replace("]", "").trim();
            if (f239a) {
                y.c(b, "xmlText = ", str3);
            }
            a(new File(str));
            FileWriter fileWriter = new FileWriter(str, true);
            fileWriter.write(str3);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void a(JSONObject jSONObject, JSONArray jSONArray, JSONArray jSONArray2, float f, String str, String str2) throws JSONException {
        try {
            jSONObject.put("s", f);
            jSONObject.put("t", str);
            jSONObject.put("w", str2);
            jSONArray2.put(jSONObject);
            jSONArray.put(new JSONObject().put("c", jSONArray2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static boolean a(File file) {
        if (!file.exists()) {
            b(file.getParentFile());
        }
        file.delete();
        return file.createNewFile();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static JSONObject b(String str, float f, String str2) throws XmlPullParserException, JSONException, IOException {
        String strSubstring;
        if (f239a) {
            y.c("ResultTOJsonUtil", "result before format : ", str);
        }
        String strTrim = str.replace("[", "").replace("]", "").trim();
        a(str2, str);
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        JSONArray jSONArray2 = new JSONArray();
        XmlPullParser xmlPullParserNewPullParser = XmlPullParserFactory.newInstance().newPullParser();
        xmlPullParserNewPullParser.setInput(new FileInputStream(new File(str2)), com.unisound.b.f.b);
        for (int eventType = xmlPullParserNewPullParser.getEventType(); eventType != 1; eventType = xmlPullParserNewPullParser.next()) {
            switch (eventType) {
                case 0:
                    if (f239a) {
                        y.c("ResultTOJsonUtil", "START_DOCUMENT : ", xmlPullParserNewPullParser.getName());
                    }
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                        y.e("ResultTOJsonUtil", "TAG 名称不全 ****************");
                        break;
                    }
                case 1:
                default:
                case 2:
                    if (f239a) {
                        y.c("ResultTOJsonUtil", "START_TAG : ", xmlPullParserNewPullParser.getName());
                    }
                    String name = xmlPullParserNewPullParser.getName();
                    if (!"s".equals(xmlPullParserNewPullParser.getName())) {
                        String name2 = xmlPullParserNewPullParser.getName();
                        try {
                            strSubstring = xmlPullParserNewPullParser.nextText();
                        } catch (Exception e2) {
                            y.e("ResultTOJsonUtil", name, "的nextText不存在或其TAG没有正常结束 ********");
                            e2.printStackTrace();
                            strSubstring = strTrim.substring(strTrim.lastIndexOf(">") + 1);
                            y.e("ResultTOJsonUtil", "MissedTagValue =", strSubstring);
                            if ("".equals(strSubstring)) {
                                strSubstring = "";
                            } else if (strSubstring.contains("<")) {
                                strSubstring = strSubstring.substring(0, strSubstring.indexOf("<"));
                            }
                        }
                        if (!"".endsWith(strSubstring)) {
                            if (f239a) {
                                y.c("ResultTOJsonUtil", "START_TAG : tagName =", name2, ", tagValue =", strSubstring);
                            }
                            a(new JSONObject(), jSONArray, new JSONArray(), 0.0f, name2, strSubstring);
                        }
                    }
                case 3:
                    if (f239a) {
                        y.c("ResultTOJsonUtil", "END_TAG : ", xmlPullParserNewPullParser.getName());
                    }
            }
        }
        jSONObject.put("l", jSONArray);
        jSONObject.put("score", f);
        jSONArray2.put(jSONObject);
        return new JSONObject().put("c", jSONArray2);
    }

    private static void b(File file) {
        if (!file.getParentFile().exists()) {
            b(file.getParentFile());
        }
        file.mkdir();
    }
}
