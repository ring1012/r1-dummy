package com.google.android.exoplayer2.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public final class XmlPullParserUtil {
    private XmlPullParserUtil() {
    }

    public static boolean isEndTag(XmlPullParser xpp, String name) throws XmlPullParserException {
        return isEndTag(xpp) && xpp.getName().equals(name);
    }

    public static boolean isEndTag(XmlPullParser xpp) throws XmlPullParserException {
        return xpp.getEventType() == 3;
    }

    public static boolean isStartTag(XmlPullParser xpp, String name) throws XmlPullParserException {
        return isStartTag(xpp) && xpp.getName().equals(name);
    }

    public static boolean isStartTag(XmlPullParser xpp) throws XmlPullParserException {
        return xpp.getEventType() == 2;
    }

    public static String getAttributeValue(XmlPullParser xpp, String attributeName) {
        int attributeCount = xpp.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            if (attributeName.equals(xpp.getAttributeName(i))) {
                return xpp.getAttributeValue(i);
            }
        }
        return null;
    }
}
