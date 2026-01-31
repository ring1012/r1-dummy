package com.google.android.exoplayer2.source.hls;

import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.webvtt.WebvttParserUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
final class WebvttExtractor implements Extractor {
    private static final Pattern LOCAL_TIMESTAMP = Pattern.compile("LOCAL:([^,]+)");
    private static final Pattern MEDIA_TIMESTAMP = Pattern.compile("MPEGTS:(\\d+)");
    private final String language;
    private ExtractorOutput output;
    private int sampleSize;
    private final TimestampAdjuster timestampAdjuster;
    private final ParsableByteArray sampleDataWrapper = new ParsableByteArray();
    private byte[] sampleData = new byte[1024];

    public WebvttExtractor(String language, TimestampAdjuster timestampAdjuster) {
        this.language = language;
        this.timestampAdjuster = timestampAdjuster;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.output = output;
        output.seekMap(new SeekMap.Unseekable(C.TIME_UNSET));
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException, NumberFormatException {
        int currentFileSize = (int) input.getLength();
        if (this.sampleSize == this.sampleData.length) {
            this.sampleData = Arrays.copyOf(this.sampleData, ((currentFileSize != -1 ? currentFileSize : this.sampleData.length) * 3) / 2);
        }
        int bytesRead = input.read(this.sampleData, this.sampleSize, this.sampleData.length - this.sampleSize);
        if (bytesRead != -1) {
            this.sampleSize += bytesRead;
            if (currentFileSize == -1 || this.sampleSize != currentFileSize) {
                return 0;
            }
        }
        processSample();
        return -1;
    }

    private void processSample() throws ParserException, NumberFormatException {
        ParsableByteArray webvttData = new ParsableByteArray(this.sampleData);
        try {
            WebvttParserUtil.validateWebvttHeaderLine(webvttData);
            long vttTimestampUs = 0;
            long tsTimestampUs = 0;
            while (true) {
                String line = webvttData.readLine();
                if (!TextUtils.isEmpty(line)) {
                    if (line.startsWith("X-TIMESTAMP-MAP")) {
                        Matcher localTimestampMatcher = LOCAL_TIMESTAMP.matcher(line);
                        if (!localTimestampMatcher.find()) {
                            throw new ParserException("X-TIMESTAMP-MAP doesn't contain local timestamp: " + line);
                        }
                        Matcher mediaTimestampMatcher = MEDIA_TIMESTAMP.matcher(line);
                        if (!mediaTimestampMatcher.find()) {
                            throw new ParserException("X-TIMESTAMP-MAP doesn't contain media timestamp: " + line);
                        }
                        vttTimestampUs = WebvttParserUtil.parseTimestampUs(localTimestampMatcher.group(1));
                        tsTimestampUs = TimestampAdjuster.ptsToUs(Long.parseLong(mediaTimestampMatcher.group(1)));
                    }
                } else {
                    Matcher cueHeaderMatcher = WebvttParserUtil.findNextCueHeader(webvttData);
                    if (cueHeaderMatcher == null) {
                        buildTrackOutput(0L);
                        return;
                    }
                    long firstCueTimeUs = WebvttParserUtil.parseTimestampUs(cueHeaderMatcher.group(1));
                    long sampleTimeUs = this.timestampAdjuster.adjustSampleTimestamp((firstCueTimeUs + tsTimestampUs) - vttTimestampUs);
                    long subsampleOffsetUs = sampleTimeUs - firstCueTimeUs;
                    TrackOutput trackOutput = buildTrackOutput(subsampleOffsetUs);
                    this.sampleDataWrapper.reset(this.sampleData, this.sampleSize);
                    trackOutput.sampleData(this.sampleDataWrapper, this.sampleSize);
                    trackOutput.sampleMetadata(sampleTimeUs, 1, this.sampleSize, 0, null);
                    return;
                }
            }
        } catch (SubtitleDecoderException e) {
            throw new ParserException(e);
        }
    }

    private TrackOutput buildTrackOutput(long subsampleOffsetUs) {
        TrackOutput trackOutput = this.output.track(0, 3);
        trackOutput.format(Format.createTextSampleFormat((String) null, MimeTypes.TEXT_VTT, (String) null, -1, 0, this.language, (DrmInitData) null, subsampleOffsetUs));
        this.output.endTracks();
        return trackOutput;
    }
}
