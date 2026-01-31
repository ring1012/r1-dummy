package com.google.android.exoplayer2.text.ttml;

import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import cn.yunzhisheng.common.PinyinConverter;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
final class TtmlRenderUtil {
    public static TtmlStyle resolveStyle(TtmlStyle style, String[] styleIds, Map<String, TtmlStyle> globalStyles) {
        if (style == null && styleIds == null) {
            return null;
        }
        if (style == null && styleIds.length == 1) {
            return globalStyles.get(styleIds[0]);
        }
        if (style == null && styleIds.length > 1) {
            TtmlStyle chainedStyle = new TtmlStyle();
            for (String id : styleIds) {
                chainedStyle.chain(globalStyles.get(id));
            }
            return chainedStyle;
        }
        if (style != null && styleIds != null && styleIds.length == 1) {
            return style.chain(globalStyles.get(styleIds[0]));
        }
        if (style != null && styleIds != null && styleIds.length > 1) {
            for (String id2 : styleIds) {
                style.chain(globalStyles.get(id2));
            }
            return style;
        }
        return style;
    }

    public static void applyStylesToSpan(SpannableStringBuilder builder, int start, int end, TtmlStyle style) {
        if (style.getStyle() != -1) {
            builder.setSpan(new StyleSpan(style.getStyle()), start, end, 33);
        }
        if (style.isLinethrough()) {
            builder.setSpan(new StrikethroughSpan(), start, end, 33);
        }
        if (style.isUnderline()) {
            builder.setSpan(new UnderlineSpan(), start, end, 33);
        }
        if (style.hasFontColor()) {
            builder.setSpan(new ForegroundColorSpan(style.getFontColor()), start, end, 33);
        }
        if (style.hasBackgroundColor()) {
            builder.setSpan(new BackgroundColorSpan(style.getBackgroundColor()), start, end, 33);
        }
        if (style.getFontFamily() != null) {
            builder.setSpan(new TypefaceSpan(style.getFontFamily()), start, end, 33);
        }
        if (style.getTextAlign() != null) {
            builder.setSpan(new AlignmentSpan.Standard(style.getTextAlign()), start, end, 33);
        }
        switch (style.getFontSizeUnit()) {
            case 1:
                builder.setSpan(new AbsoluteSizeSpan((int) style.getFontSize(), true), start, end, 33);
                break;
            case 2:
                builder.setSpan(new RelativeSizeSpan(style.getFontSize()), start, end, 33);
                break;
            case 3:
                builder.setSpan(new RelativeSizeSpan(style.getFontSize() / 100.0f), start, end, 33);
                break;
        }
    }

    static void endParagraph(SpannableStringBuilder builder) {
        int position = builder.length() - 1;
        while (position >= 0 && builder.charAt(position) == ' ') {
            position--;
        }
        if (position >= 0 && builder.charAt(position) != '\n') {
            builder.append('\n');
        }
    }

    static String applyTextElementSpacePolicy(String in) {
        String out = in.replaceAll(IOUtils.LINE_SEPARATOR_WINDOWS, "\n");
        return out.replaceAll(" *\n *", "\n").replaceAll("\n", PinyinConverter.PINYIN_SEPARATOR).replaceAll("[ \t\\x0B\f\r]+", PinyinConverter.PINYIN_SEPARATOR);
    }

    private TtmlRenderUtil() {
    }
}
