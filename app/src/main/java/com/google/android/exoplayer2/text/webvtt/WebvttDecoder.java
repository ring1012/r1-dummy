package com.google.android.exoplayer2.text.webvtt;

import android.text.TextUtils;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.webvtt.WebvttCue;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class WebvttDecoder extends SimpleSubtitleDecoder {
    private static final String COMMENT_START = "NOTE";
    private static final int EVENT_COMMENT = 1;
    private static final int EVENT_CUE = 3;
    private static final int EVENT_END_OF_FILE = 0;
    private static final int EVENT_NONE = -1;
    private static final int EVENT_STYLE_BLOCK = 2;
    private static final String STYLE_START = "STYLE";
    private final CssParser cssParser;
    private final WebvttCueParser cueParser;
    private final List<WebvttCssStyle> definedStyles;
    private final ParsableByteArray parsableWebvttData;
    private final WebvttCue.Builder webvttCueBuilder;

    public WebvttDecoder() {
        super("WebvttDecoder");
        this.cueParser = new WebvttCueParser();
        this.parsableWebvttData = new ParsableByteArray();
        this.webvttCueBuilder = new WebvttCue.Builder();
        this.cssParser = new CssParser();
        this.definedStyles = new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.text.SimpleSubtitleDecoder
    public WebvttSubtitle decode(byte[] bytes, int length, boolean reset) throws SubtitleDecoderException {
        this.parsableWebvttData.reset(bytes, length);
        this.webvttCueBuilder.reset();
        this.definedStyles.clear();
        WebvttParserUtil.validateWebvttHeaderLine(this.parsableWebvttData);
        while (!TextUtils.isEmpty(this.parsableWebvttData.readLine())) {
        }
        ArrayList<WebvttCue> subtitles = new ArrayList<>();
        while (true) {
            int event = getNextEvent(this.parsableWebvttData);
            if (event != 0) {
                if (event == 1) {
                    skipComment(this.parsableWebvttData);
                } else if (event == 2) {
                    if (!subtitles.isEmpty()) {
                        throw new SubtitleDecoderException("A style block was found after the first cue.");
                    }
                    this.parsableWebvttData.readLine();
                    WebvttCssStyle styleBlock = this.cssParser.parseBlock(this.parsableWebvttData);
                    if (styleBlock != null) {
                        this.definedStyles.add(styleBlock);
                    }
                } else if (event == 3 && this.cueParser.parseCue(this.parsableWebvttData, this.webvttCueBuilder, this.definedStyles)) {
                    subtitles.add(this.webvttCueBuilder.build());
                    this.webvttCueBuilder.reset();
                }
            } else {
                return new WebvttSubtitle(subtitles);
            }
        }
    }

    private static int getNextEvent(ParsableByteArray parsableWebvttData) {
        int foundEvent = -1;
        int currentInputPosition = 0;
        while (foundEvent == -1) {
            currentInputPosition = parsableWebvttData.getPosition();
            String line = parsableWebvttData.readLine();
            if (line == null) {
                foundEvent = 0;
            } else if (STYLE_START.equals(line)) {
                foundEvent = 2;
            } else if (COMMENT_START.startsWith(line)) {
                foundEvent = 1;
            } else {
                foundEvent = 3;
            }
        }
        parsableWebvttData.setPosition(currentInputPosition);
        return foundEvent;
    }

    private static void skipComment(ParsableByteArray parsableWebvttData) {
        while (!TextUtils.isEmpty(parsableWebvttData.readLine())) {
        }
    }
}
