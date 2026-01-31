package com.google.android.exoplayer2.text.subrip;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class SubripDecoder extends SimpleSubtitleDecoder {
    private static final String SUBRIP_TIMECODE = "(?:(\\d+):)?(\\d+):(\\d+),(\\d+)";
    private static final Pattern SUBRIP_TIMING_LINE = Pattern.compile("\\s*((?:(\\d+):)?(\\d+):(\\d+),(\\d+))\\s*-->\\s*((?:(\\d+):)?(\\d+):(\\d+),(\\d+))?\\s*");
    private static final String TAG = "SubripDecoder";
    private final StringBuilder textBuilder;

    public SubripDecoder() {
        super(TAG);
        this.textBuilder = new StringBuilder();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.text.SimpleSubtitleDecoder
    public SubripSubtitle decode(byte[] bytes, int length, boolean reset) throws NumberFormatException {
        ArrayList<Cue> cues = new ArrayList<>();
        LongArray cueTimesUs = new LongArray();
        ParsableByteArray subripData = new ParsableByteArray(bytes, length);
        while (true) {
            String currentLine = subripData.readLine();
            if (currentLine != null) {
                if (currentLine.length() != 0) {
                    try {
                        Integer.parseInt(currentLine);
                        boolean haveEndTimecode = false;
                        String currentLine2 = subripData.readLine();
                        Matcher matcher = SUBRIP_TIMING_LINE.matcher(currentLine2);
                        if (matcher.matches()) {
                            cueTimesUs.add(parseTimecode(matcher, 1));
                            if (!TextUtils.isEmpty(matcher.group(6))) {
                                haveEndTimecode = true;
                                cueTimesUs.add(parseTimecode(matcher, 6));
                            }
                            this.textBuilder.setLength(0);
                            while (true) {
                                String currentLine3 = subripData.readLine();
                                if (TextUtils.isEmpty(currentLine3)) {
                                    break;
                                }
                                if (this.textBuilder.length() > 0) {
                                    this.textBuilder.append("<br>");
                                }
                                this.textBuilder.append(currentLine3.trim());
                            }
                            Spanned text = Html.fromHtml(this.textBuilder.toString());
                            cues.add(new Cue(text));
                            if (haveEndTimecode) {
                                cues.add(null);
                            }
                        } else {
                            Log.w(TAG, "Skipping invalid timing: " + currentLine2);
                        }
                    } catch (NumberFormatException e) {
                        Log.w(TAG, "Skipping invalid index: " + currentLine);
                    }
                }
            } else {
                Cue[] cuesArray = new Cue[cues.size()];
                cues.toArray(cuesArray);
                long[] cueTimesUsArray = cueTimesUs.toArray();
                return new SubripSubtitle(cuesArray, cueTimesUsArray);
            }
        }
    }

    private static long parseTimecode(Matcher matcher, int groupOffset) {
        long timestampMs = Long.parseLong(matcher.group(groupOffset + 1)) * 60 * 60 * 1000;
        return (timestampMs + (Long.parseLong(matcher.group(groupOffset + 2)) * 60 * 1000) + (Long.parseLong(matcher.group(groupOffset + 3)) * 1000) + Long.parseLong(matcher.group(groupOffset + 4))) * 1000;
    }
}
