package com.google.android.exoplayer2.trackselection;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.unisound.vui.priority.PriorityMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class DefaultTrackSelector extends MappingTrackSelector {
    private static final float FRACTION_TO_CONSIDER_FULLSCREEN = 0.98f;
    private static final int[] NO_TRACKS = new int[0];
    private static final int WITHIN_RENDERER_CAPABILITIES_BONUS = 1000;
    private final TrackSelection.Factory adaptiveTrackSelectionFactory;
    private final AtomicReference<Parameters> paramsReference;

    public static final class Parameters {
        public final boolean allowMixedMimeAdaptiveness;
        public final boolean allowNonSeamlessAdaptiveness;
        public final boolean exceedRendererCapabilitiesIfNecessary;
        public final boolean exceedVideoConstraintsIfNecessary;
        public final int maxVideoBitrate;
        public final int maxVideoHeight;
        public final int maxVideoWidth;
        public final boolean orientationMayChange;
        public final String preferredAudioLanguage;
        public final String preferredTextLanguage;
        public final int viewportHeight;
        public final int viewportWidth;

        public Parameters() {
            this(null, null, false, true, PriorityMap.PRIORITY_MAX, PriorityMap.PRIORITY_MAX, PriorityMap.PRIORITY_MAX, true, true, PriorityMap.PRIORITY_MAX, PriorityMap.PRIORITY_MAX, true);
        }

        public Parameters(String preferredAudioLanguage, String preferredTextLanguage, boolean allowMixedMimeAdaptiveness, boolean allowNonSeamlessAdaptiveness, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, boolean exceedVideoConstraintsIfNecessary, boolean exceedRendererCapabilitiesIfNecessary, int viewportWidth, int viewportHeight, boolean orientationMayChange) {
            this.preferredAudioLanguage = preferredAudioLanguage;
            this.preferredTextLanguage = preferredTextLanguage;
            this.allowMixedMimeAdaptiveness = allowMixedMimeAdaptiveness;
            this.allowNonSeamlessAdaptiveness = allowNonSeamlessAdaptiveness;
            this.maxVideoWidth = maxVideoWidth;
            this.maxVideoHeight = maxVideoHeight;
            this.maxVideoBitrate = maxVideoBitrate;
            this.exceedVideoConstraintsIfNecessary = exceedVideoConstraintsIfNecessary;
            this.exceedRendererCapabilitiesIfNecessary = exceedRendererCapabilitiesIfNecessary;
            this.viewportWidth = viewportWidth;
            this.viewportHeight = viewportHeight;
            this.orientationMayChange = orientationMayChange;
        }

        public Parameters withPreferredAudioLanguage(String preferredAudioLanguage) {
            String preferredAudioLanguage2 = Util.normalizeLanguageCode(preferredAudioLanguage);
            return TextUtils.equals(preferredAudioLanguage2, this.preferredAudioLanguage) ? this : new Parameters(preferredAudioLanguage2, this.preferredTextLanguage, this.allowMixedMimeAdaptiveness, this.allowNonSeamlessAdaptiveness, this.maxVideoWidth, this.maxVideoHeight, this.maxVideoBitrate, this.exceedVideoConstraintsIfNecessary, this.exceedRendererCapabilitiesIfNecessary, this.viewportWidth, this.viewportHeight, this.orientationMayChange);
        }

        public Parameters withPreferredTextLanguage(String preferredTextLanguage) {
            String preferredTextLanguage2 = Util.normalizeLanguageCode(preferredTextLanguage);
            return TextUtils.equals(preferredTextLanguage2, this.preferredTextLanguage) ? this : new Parameters(this.preferredAudioLanguage, preferredTextLanguage2, this.allowMixedMimeAdaptiveness, this.allowNonSeamlessAdaptiveness, this.maxVideoWidth, this.maxVideoHeight, this.maxVideoBitrate, this.exceedVideoConstraintsIfNecessary, this.exceedRendererCapabilitiesIfNecessary, this.viewportWidth, this.viewportHeight, this.orientationMayChange);
        }

        public Parameters withAllowMixedMimeAdaptiveness(boolean allowMixedMimeAdaptiveness) {
            return allowMixedMimeAdaptiveness == this.allowMixedMimeAdaptiveness ? this : new Parameters(this.preferredAudioLanguage, this.preferredTextLanguage, allowMixedMimeAdaptiveness, this.allowNonSeamlessAdaptiveness, this.maxVideoWidth, this.maxVideoHeight, this.maxVideoBitrate, this.exceedVideoConstraintsIfNecessary, this.exceedRendererCapabilitiesIfNecessary, this.viewportWidth, this.viewportHeight, this.orientationMayChange);
        }

        public Parameters withAllowNonSeamlessAdaptiveness(boolean allowNonSeamlessAdaptiveness) {
            return allowNonSeamlessAdaptiveness == this.allowNonSeamlessAdaptiveness ? this : new Parameters(this.preferredAudioLanguage, this.preferredTextLanguage, this.allowMixedMimeAdaptiveness, allowNonSeamlessAdaptiveness, this.maxVideoWidth, this.maxVideoHeight, this.maxVideoBitrate, this.exceedVideoConstraintsIfNecessary, this.exceedRendererCapabilitiesIfNecessary, this.viewportWidth, this.viewportHeight, this.orientationMayChange);
        }

        public Parameters withMaxVideoSize(int maxVideoWidth, int maxVideoHeight) {
            return (maxVideoWidth == this.maxVideoWidth && maxVideoHeight == this.maxVideoHeight) ? this : new Parameters(this.preferredAudioLanguage, this.preferredTextLanguage, this.allowMixedMimeAdaptiveness, this.allowNonSeamlessAdaptiveness, maxVideoWidth, maxVideoHeight, this.maxVideoBitrate, this.exceedVideoConstraintsIfNecessary, this.exceedRendererCapabilitiesIfNecessary, this.viewportWidth, this.viewportHeight, this.orientationMayChange);
        }

        public Parameters withMaxVideoBitrate(int maxVideoBitrate) {
            return maxVideoBitrate == this.maxVideoBitrate ? this : new Parameters(this.preferredAudioLanguage, this.preferredTextLanguage, this.allowMixedMimeAdaptiveness, this.allowNonSeamlessAdaptiveness, this.maxVideoWidth, this.maxVideoHeight, maxVideoBitrate, this.exceedVideoConstraintsIfNecessary, this.exceedRendererCapabilitiesIfNecessary, this.viewportWidth, this.viewportHeight, this.orientationMayChange);
        }

        public Parameters withMaxVideoSizeSd() {
            return withMaxVideoSize(1279, 719);
        }

        public Parameters withoutVideoSizeConstraints() {
            return withMaxVideoSize(PriorityMap.PRIORITY_MAX, PriorityMap.PRIORITY_MAX);
        }

        public Parameters withExceedVideoConstraintsIfNecessary(boolean exceedVideoConstraintsIfNecessary) {
            return exceedVideoConstraintsIfNecessary == this.exceedVideoConstraintsIfNecessary ? this : new Parameters(this.preferredAudioLanguage, this.preferredTextLanguage, this.allowMixedMimeAdaptiveness, this.allowNonSeamlessAdaptiveness, this.maxVideoWidth, this.maxVideoHeight, this.maxVideoBitrate, exceedVideoConstraintsIfNecessary, this.exceedRendererCapabilitiesIfNecessary, this.viewportWidth, this.viewportHeight, this.orientationMayChange);
        }

        public Parameters withExceedRendererCapabilitiesIfNecessary(boolean exceedRendererCapabilitiesIfNecessary) {
            return exceedRendererCapabilitiesIfNecessary == this.exceedRendererCapabilitiesIfNecessary ? this : new Parameters(this.preferredAudioLanguage, this.preferredTextLanguage, this.allowMixedMimeAdaptiveness, this.allowNonSeamlessAdaptiveness, this.maxVideoWidth, this.maxVideoHeight, this.maxVideoBitrate, this.exceedVideoConstraintsIfNecessary, exceedRendererCapabilitiesIfNecessary, this.viewportWidth, this.viewportHeight, this.orientationMayChange);
        }

        public Parameters withViewportSize(int viewportWidth, int viewportHeight, boolean orientationMayChange) {
            return (viewportWidth == this.viewportWidth && viewportHeight == this.viewportHeight && orientationMayChange == this.orientationMayChange) ? this : new Parameters(this.preferredAudioLanguage, this.preferredTextLanguage, this.allowMixedMimeAdaptiveness, this.allowNonSeamlessAdaptiveness, this.maxVideoWidth, this.maxVideoHeight, this.maxVideoBitrate, this.exceedVideoConstraintsIfNecessary, this.exceedRendererCapabilitiesIfNecessary, viewportWidth, viewportHeight, orientationMayChange);
        }

        public Parameters withViewportSizeFromContext(Context context, boolean orientationMayChange) {
            Point viewportSize = Util.getPhysicalDisplaySize(context);
            return withViewportSize(viewportSize.x, viewportSize.y, orientationMayChange);
        }

        public Parameters withoutViewportSizeConstraints() {
            return withViewportSize(PriorityMap.PRIORITY_MAX, PriorityMap.PRIORITY_MAX, true);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Parameters other = (Parameters) obj;
            return this.allowMixedMimeAdaptiveness == other.allowMixedMimeAdaptiveness && this.allowNonSeamlessAdaptiveness == other.allowNonSeamlessAdaptiveness && this.maxVideoWidth == other.maxVideoWidth && this.maxVideoHeight == other.maxVideoHeight && this.exceedVideoConstraintsIfNecessary == other.exceedVideoConstraintsIfNecessary && this.exceedRendererCapabilitiesIfNecessary == other.exceedRendererCapabilitiesIfNecessary && this.orientationMayChange == other.orientationMayChange && this.viewportWidth == other.viewportWidth && this.viewportHeight == other.viewportHeight && this.maxVideoBitrate == other.maxVideoBitrate && TextUtils.equals(this.preferredAudioLanguage, other.preferredAudioLanguage) && TextUtils.equals(this.preferredTextLanguage, other.preferredTextLanguage);
        }

        public int hashCode() {
            int result = this.preferredAudioLanguage.hashCode();
            return (((((((((((((((((((((result * 31) + this.preferredTextLanguage.hashCode()) * 31) + (this.allowMixedMimeAdaptiveness ? 1 : 0)) * 31) + (this.allowNonSeamlessAdaptiveness ? 1 : 0)) * 31) + this.maxVideoWidth) * 31) + this.maxVideoHeight) * 31) + this.maxVideoBitrate) * 31) + (this.exceedVideoConstraintsIfNecessary ? 1 : 0)) * 31) + (this.exceedRendererCapabilitiesIfNecessary ? 1 : 0)) * 31) + (this.orientationMayChange ? 1 : 0)) * 31) + this.viewportWidth) * 31) + this.viewportHeight;
        }
    }

    public DefaultTrackSelector() {
        this(null);
    }

    public DefaultTrackSelector(TrackSelection.Factory adaptiveTrackSelectionFactory) {
        this.adaptiveTrackSelectionFactory = adaptiveTrackSelectionFactory;
        this.paramsReference = new AtomicReference<>(new Parameters());
    }

    public void setParameters(Parameters params) {
        Assertions.checkNotNull(params);
        if (!this.paramsReference.getAndSet(params).equals(params)) {
            invalidate();
        }
    }

    public Parameters getParameters() {
        return this.paramsReference.get();
    }

    @Override // com.google.android.exoplayer2.trackselection.MappingTrackSelector
    protected TrackSelection[] selectTracks(RendererCapabilities[] rendererCapabilities, TrackGroupArray[] rendererTrackGroupArrays, int[][][] rendererFormatSupports) throws ExoPlaybackException {
        int rendererCount = rendererCapabilities.length;
        TrackSelection[] rendererTrackSelections = new TrackSelection[rendererCount];
        Parameters params = this.paramsReference.get();
        boolean videoTrackAndRendererPresent = false;
        for (int i = 0; i < rendererCount; i++) {
            if (2 == rendererCapabilities[i].getTrackType()) {
                rendererTrackSelections[i] = selectVideoTrack(rendererCapabilities[i], rendererTrackGroupArrays[i], rendererFormatSupports[i], params.maxVideoWidth, params.maxVideoHeight, params.maxVideoBitrate, params.allowNonSeamlessAdaptiveness, params.allowMixedMimeAdaptiveness, params.viewportWidth, params.viewportHeight, params.orientationMayChange, this.adaptiveTrackSelectionFactory, params.exceedVideoConstraintsIfNecessary, params.exceedRendererCapabilitiesIfNecessary);
                videoTrackAndRendererPresent |= rendererTrackGroupArrays[i].length > 0;
            }
        }
        for (int i2 = 0; i2 < rendererCount; i2++) {
            switch (rendererCapabilities[i2].getTrackType()) {
                case 1:
                    rendererTrackSelections[i2] = selectAudioTrack(rendererTrackGroupArrays[i2], rendererFormatSupports[i2], params.preferredAudioLanguage, params.exceedRendererCapabilitiesIfNecessary, params.allowMixedMimeAdaptiveness, videoTrackAndRendererPresent ? null : this.adaptiveTrackSelectionFactory);
                    break;
                case 2:
                    break;
                case 3:
                    rendererTrackSelections[i2] = selectTextTrack(rendererTrackGroupArrays[i2], rendererFormatSupports[i2], params.preferredTextLanguage, params.preferredAudioLanguage, params.exceedRendererCapabilitiesIfNecessary);
                    break;
                default:
                    rendererTrackSelections[i2] = selectOtherTrack(rendererCapabilities[i2].getTrackType(), rendererTrackGroupArrays[i2], rendererFormatSupports[i2], params.exceedRendererCapabilitiesIfNecessary);
                    break;
            }
        }
        return rendererTrackSelections;
    }

    protected TrackSelection selectVideoTrack(RendererCapabilities rendererCapabilities, TrackGroupArray groups, int[][] formatSupport, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, boolean allowNonSeamlessAdaptiveness, boolean allowMixedMimeAdaptiveness, int viewportWidth, int viewportHeight, boolean orientationMayChange, TrackSelection.Factory adaptiveTrackSelectionFactory, boolean exceedConstraintsIfNecessary, boolean exceedRendererCapabilitiesIfNecessary) throws ExoPlaybackException {
        TrackSelection selection = null;
        if (adaptiveTrackSelectionFactory != null) {
            selection = selectAdaptiveVideoTrack(rendererCapabilities, groups, formatSupport, maxVideoWidth, maxVideoHeight, maxVideoBitrate, allowNonSeamlessAdaptiveness, allowMixedMimeAdaptiveness, viewportWidth, viewportHeight, orientationMayChange, adaptiveTrackSelectionFactory);
        }
        if (selection == null) {
            TrackSelection selection2 = selectFixedVideoTrack(groups, formatSupport, maxVideoWidth, maxVideoHeight, maxVideoBitrate, viewportWidth, viewportHeight, orientationMayChange, exceedConstraintsIfNecessary, exceedRendererCapabilitiesIfNecessary);
            return selection2;
        }
        return selection;
    }

    private static TrackSelection selectAdaptiveVideoTrack(RendererCapabilities rendererCapabilities, TrackGroupArray groups, int[][] formatSupport, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, boolean allowNonSeamlessAdaptiveness, boolean allowMixedMimeAdaptiveness, int viewportWidth, int viewportHeight, boolean orientationMayChange, TrackSelection.Factory adaptiveTrackSelectionFactory) throws ExoPlaybackException {
        int requiredAdaptiveSupport = allowNonSeamlessAdaptiveness ? 12 : 8;
        boolean allowMixedMimeTypes = allowMixedMimeAdaptiveness && (rendererCapabilities.supportsMixedMimeTypeAdaptation() & requiredAdaptiveSupport) != 0;
        for (int i = 0; i < groups.length; i++) {
            TrackGroup group = groups.get(i);
            int[] adaptiveTracks = getAdaptiveVideoTracksForGroup(group, formatSupport[i], allowMixedMimeTypes, requiredAdaptiveSupport, maxVideoWidth, maxVideoHeight, maxVideoBitrate, viewportWidth, viewportHeight, orientationMayChange);
            if (adaptiveTracks.length > 0) {
                return adaptiveTrackSelectionFactory.createTrackSelection(group, adaptiveTracks);
            }
        }
        return null;
    }

    private static int[] getAdaptiveVideoTracksForGroup(TrackGroup group, int[] formatSupport, boolean allowMixedMimeTypes, int requiredAdaptiveSupport, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, int viewportWidth, int viewportHeight, boolean orientationMayChange) {
        if (group.length < 2) {
            return NO_TRACKS;
        }
        List<Integer> selectedTrackIndices = getViewportFilteredTrackIndices(group, viewportWidth, viewportHeight, orientationMayChange);
        if (selectedTrackIndices.size() < 2) {
            return NO_TRACKS;
        }
        String selectedMimeType = null;
        if (!allowMixedMimeTypes) {
            HashSet<String> seenMimeTypes = new HashSet<>();
            int selectedMimeTypeTrackCount = 0;
            for (int i = 0; i < selectedTrackIndices.size(); i++) {
                int trackIndex = selectedTrackIndices.get(i).intValue();
                String sampleMimeType = group.getFormat(trackIndex).sampleMimeType;
                if (seenMimeTypes.add(sampleMimeType)) {
                    int countForMimeType = getAdaptiveVideoTrackCountForMimeType(group, formatSupport, requiredAdaptiveSupport, sampleMimeType, maxVideoWidth, maxVideoHeight, maxVideoBitrate, selectedTrackIndices);
                    if (countForMimeType > selectedMimeTypeTrackCount) {
                        selectedMimeType = sampleMimeType;
                        selectedMimeTypeTrackCount = countForMimeType;
                    }
                }
            }
        }
        filterAdaptiveVideoTrackCountForMimeType(group, formatSupport, requiredAdaptiveSupport, selectedMimeType, maxVideoWidth, maxVideoHeight, maxVideoBitrate, selectedTrackIndices);
        return selectedTrackIndices.size() < 2 ? NO_TRACKS : Util.toArray(selectedTrackIndices);
    }

    private static int getAdaptiveVideoTrackCountForMimeType(TrackGroup group, int[] formatSupport, int requiredAdaptiveSupport, String mimeType, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, List<Integer> selectedTrackIndices) {
        int adaptiveTrackCount = 0;
        for (int i = 0; i < selectedTrackIndices.size(); i++) {
            int trackIndex = selectedTrackIndices.get(i).intValue();
            if (isSupportedAdaptiveVideoTrack(group.getFormat(trackIndex), mimeType, formatSupport[trackIndex], requiredAdaptiveSupport, maxVideoWidth, maxVideoHeight, maxVideoBitrate)) {
                adaptiveTrackCount++;
            }
        }
        return adaptiveTrackCount;
    }

    private static void filterAdaptiveVideoTrackCountForMimeType(TrackGroup group, int[] formatSupport, int requiredAdaptiveSupport, String mimeType, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, List<Integer> selectedTrackIndices) {
        for (int i = selectedTrackIndices.size() - 1; i >= 0; i--) {
            int trackIndex = selectedTrackIndices.get(i).intValue();
            if (!isSupportedAdaptiveVideoTrack(group.getFormat(trackIndex), mimeType, formatSupport[trackIndex], requiredAdaptiveSupport, maxVideoWidth, maxVideoHeight, maxVideoBitrate)) {
                selectedTrackIndices.remove(i);
            }
        }
    }

    private static boolean isSupportedAdaptiveVideoTrack(Format format, String mimeType, int formatSupport, int requiredAdaptiveSupport, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate) {
        if (!isSupported(formatSupport, false) || (formatSupport & requiredAdaptiveSupport) == 0) {
            return false;
        }
        if (mimeType != null && !Util.areEqual(format.sampleMimeType, mimeType)) {
            return false;
        }
        if (format.width != -1 && format.width > maxVideoWidth) {
            return false;
        }
        if (format.height == -1 || format.height <= maxVideoHeight) {
            return format.bitrate == -1 || format.bitrate <= maxVideoBitrate;
        }
        return false;
    }

    private static TrackSelection selectFixedVideoTrack(TrackGroupArray groups, int[][] formatSupport, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, int viewportWidth, int viewportHeight, boolean orientationMayChange, boolean exceedConstraintsIfNecessary, boolean exceedRendererCapabilitiesIfNecessary) {
        int comparisonResult;
        TrackGroup selectedGroup = null;
        int selectedTrackIndex = 0;
        int selectedTrackScore = 0;
        int selectedBitrate = -1;
        int selectedPixelCount = -1;
        for (int groupIndex = 0; groupIndex < groups.length; groupIndex++) {
            TrackGroup trackGroup = groups.get(groupIndex);
            List<Integer> selectedTrackIndices = getViewportFilteredTrackIndices(trackGroup, viewportWidth, viewportHeight, orientationMayChange);
            int[] trackFormatSupport = formatSupport[groupIndex];
            for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
                if (isSupported(trackFormatSupport[trackIndex], exceedRendererCapabilitiesIfNecessary)) {
                    Format format = trackGroup.getFormat(trackIndex);
                    boolean isWithinConstraints = selectedTrackIndices.contains(Integer.valueOf(trackIndex)) && (format.width == -1 || format.width <= maxVideoWidth) && ((format.height == -1 || format.height <= maxVideoHeight) && (format.bitrate == -1 || format.bitrate <= maxVideoBitrate));
                    if (isWithinConstraints || exceedConstraintsIfNecessary) {
                        int trackScore = isWithinConstraints ? 2 : 1;
                        if (isSupported(trackFormatSupport[trackIndex], false)) {
                            trackScore += 1000;
                        }
                        boolean selectTrack = trackScore > selectedTrackScore;
                        if (trackScore == selectedTrackScore) {
                            int formatPixelCount = format.getPixelCount();
                            if (formatPixelCount != selectedPixelCount) {
                                comparisonResult = compareFormatValues(format.getPixelCount(), selectedPixelCount);
                            } else {
                                comparisonResult = compareFormatValues(format.bitrate, selectedBitrate);
                            }
                            if (isWithinConstraints) {
                                selectTrack = comparisonResult > 0;
                            } else {
                                selectTrack = comparisonResult < 0;
                            }
                        }
                        if (selectTrack) {
                            selectedGroup = trackGroup;
                            selectedTrackIndex = trackIndex;
                            selectedTrackScore = trackScore;
                            selectedBitrate = format.bitrate;
                            selectedPixelCount = format.getPixelCount();
                        }
                    }
                }
            }
        }
        if (selectedGroup == null) {
            return null;
        }
        return new FixedTrackSelection(selectedGroup, selectedTrackIndex);
    }

    private static int compareFormatValues(int first, int second) {
        if (first == -1) {
            return second == -1 ? 0 : -1;
        }
        if (second == -1) {
            return 1;
        }
        return first - second;
    }

    protected TrackSelection selectAudioTrack(TrackGroupArray groups, int[][] formatSupport, String preferredAudioLanguage, boolean exceedRendererCapabilitiesIfNecessary, boolean allowMixedMimeAdaptiveness, TrackSelection.Factory adaptiveTrackSelectionFactory) {
        int selectedGroupIndex = -1;
        int selectedTrackIndex = -1;
        int selectedTrackScore = 0;
        for (int groupIndex = 0; groupIndex < groups.length; groupIndex++) {
            TrackGroup trackGroup = groups.get(groupIndex);
            int[] trackFormatSupport = formatSupport[groupIndex];
            for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
                if (isSupported(trackFormatSupport[trackIndex], exceedRendererCapabilitiesIfNecessary)) {
                    Format format = trackGroup.getFormat(trackIndex);
                    int trackScore = getAudioTrackScore(trackFormatSupport[trackIndex], preferredAudioLanguage, format);
                    if (trackScore > selectedTrackScore) {
                        selectedGroupIndex = groupIndex;
                        selectedTrackIndex = trackIndex;
                        selectedTrackScore = trackScore;
                    }
                }
            }
        }
        if (selectedGroupIndex == -1) {
            return null;
        }
        TrackGroup selectedGroup = groups.get(selectedGroupIndex);
        if (adaptiveTrackSelectionFactory != null) {
            int[] adaptiveTracks = getAdaptiveAudioTracks(selectedGroup, formatSupport[selectedGroupIndex], allowMixedMimeAdaptiveness);
            if (adaptiveTracks.length > 0) {
                return adaptiveTrackSelectionFactory.createTrackSelection(selectedGroup, adaptiveTracks);
            }
        }
        return new FixedTrackSelection(selectedGroup, selectedTrackIndex);
    }

    private static int getAudioTrackScore(int formatSupport, String preferredLanguage, Format format) {
        int trackScore;
        boolean isDefault = (format.selectionFlags & 1) != 0;
        if (formatHasLanguage(format, preferredLanguage)) {
            if (isDefault) {
                trackScore = 4;
            } else {
                trackScore = 3;
            }
        } else if (isDefault) {
            trackScore = 2;
        } else {
            trackScore = 1;
        }
        if (isSupported(formatSupport, false)) {
            return trackScore + 1000;
        }
        return trackScore;
    }

    private static int[] getAdaptiveAudioTracks(TrackGroup group, int[] formatSupport, boolean allowMixedMimeTypes) {
        int configurationCount;
        int selectedConfigurationTrackCount = 0;
        AudioConfigurationTuple selectedConfiguration = null;
        HashSet<AudioConfigurationTuple> seenConfigurationTuples = new HashSet<>();
        for (int i = 0; i < group.length; i++) {
            Format format = group.getFormat(i);
            AudioConfigurationTuple configuration = new AudioConfigurationTuple(format.channelCount, format.sampleRate, allowMixedMimeTypes ? null : format.sampleMimeType);
            if (seenConfigurationTuples.add(configuration) && (configurationCount = getAdaptiveAudioTrackCount(group, formatSupport, configuration)) > selectedConfigurationTrackCount) {
                selectedConfiguration = configuration;
                selectedConfigurationTrackCount = configurationCount;
            }
        }
        if (selectedConfigurationTrackCount > 1) {
            int[] adaptiveIndices = new int[selectedConfigurationTrackCount];
            int index = 0;
            for (int i2 = 0; i2 < group.length; i2++) {
                if (isSupportedAdaptiveAudioTrack(group.getFormat(i2), formatSupport[i2], selectedConfiguration)) {
                    adaptiveIndices[index] = i2;
                    index++;
                }
            }
            return adaptiveIndices;
        }
        return NO_TRACKS;
    }

    private static int getAdaptiveAudioTrackCount(TrackGroup group, int[] formatSupport, AudioConfigurationTuple configuration) {
        int count = 0;
        for (int i = 0; i < group.length; i++) {
            if (isSupportedAdaptiveAudioTrack(group.getFormat(i), formatSupport[i], configuration)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isSupportedAdaptiveAudioTrack(Format format, int formatSupport, AudioConfigurationTuple configuration) {
        if (isSupported(formatSupport, false) && format.channelCount == configuration.channelCount && format.sampleRate == configuration.sampleRate) {
            return configuration.mimeType == null || TextUtils.equals(configuration.mimeType, format.sampleMimeType);
        }
        return false;
    }

    protected TrackSelection selectTextTrack(TrackGroupArray groups, int[][] formatSupport, String preferredTextLanguage, String preferredAudioLanguage, boolean exceedRendererCapabilitiesIfNecessary) {
        int trackScore;
        TrackGroup selectedGroup = null;
        int selectedTrackIndex = 0;
        int selectedTrackScore = 0;
        for (int groupIndex = 0; groupIndex < groups.length; groupIndex++) {
            TrackGroup trackGroup = groups.get(groupIndex);
            int[] trackFormatSupport = formatSupport[groupIndex];
            for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
                if (isSupported(trackFormatSupport[trackIndex], exceedRendererCapabilitiesIfNecessary)) {
                    Format format = trackGroup.getFormat(trackIndex);
                    boolean isDefault = (format.selectionFlags & 1) != 0;
                    boolean isForced = (format.selectionFlags & 2) != 0;
                    if (formatHasLanguage(format, preferredTextLanguage)) {
                        if (isDefault) {
                            trackScore = 6;
                        } else if (!isForced) {
                            trackScore = 5;
                        } else {
                            trackScore = 4;
                        }
                    } else if (isDefault) {
                        trackScore = 3;
                    } else if (isForced) {
                        if (formatHasLanguage(format, preferredAudioLanguage)) {
                            trackScore = 2;
                        } else {
                            trackScore = 1;
                        }
                    }
                    int selectedTrackScore2 = isSupported(trackFormatSupport[trackIndex], false) ? trackScore + 1000 : trackScore;
                    if (selectedTrackScore2 > selectedTrackScore) {
                        selectedGroup = trackGroup;
                        selectedTrackIndex = trackIndex;
                        selectedTrackScore = selectedTrackScore2;
                    }
                }
            }
        }
        if (selectedGroup == null) {
            return null;
        }
        return new FixedTrackSelection(selectedGroup, selectedTrackIndex);
    }

    protected TrackSelection selectOtherTrack(int trackType, TrackGroupArray groups, int[][] formatSupport, boolean exceedRendererCapabilitiesIfNecessary) {
        TrackGroup selectedGroup = null;
        int selectedTrackIndex = 0;
        int selectedTrackScore = 0;
        for (int groupIndex = 0; groupIndex < groups.length; groupIndex++) {
            TrackGroup trackGroup = groups.get(groupIndex);
            int[] trackFormatSupport = formatSupport[groupIndex];
            for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
                if (isSupported(trackFormatSupport[trackIndex], exceedRendererCapabilitiesIfNecessary)) {
                    Format format = trackGroup.getFormat(trackIndex);
                    boolean isDefault = (format.selectionFlags & 1) != 0;
                    int trackScore = isDefault ? 2 : 1;
                    if (isSupported(trackFormatSupport[trackIndex], false)) {
                        trackScore += 1000;
                    }
                    if (trackScore > selectedTrackScore) {
                        selectedGroup = trackGroup;
                        selectedTrackIndex = trackIndex;
                        selectedTrackScore = trackScore;
                    }
                }
            }
        }
        if (selectedGroup == null) {
            return null;
        }
        return new FixedTrackSelection(selectedGroup, selectedTrackIndex);
    }

    protected static boolean isSupported(int formatSupport, boolean allowExceedsCapabilities) {
        int maskedSupport = formatSupport & 3;
        return maskedSupport == 3 || (allowExceedsCapabilities && maskedSupport == 2);
    }

    protected static boolean formatHasLanguage(Format format, String language) {
        return TextUtils.equals(language, Util.normalizeLanguageCode(format.language));
    }

    private static List<Integer> getViewportFilteredTrackIndices(TrackGroup group, int viewportWidth, int viewportHeight, boolean orientationMayChange) {
        ArrayList<Integer> selectedTrackIndices = new ArrayList<>(group.length);
        for (int i = 0; i < group.length; i++) {
            selectedTrackIndices.add(Integer.valueOf(i));
        }
        if (viewportWidth != Integer.MAX_VALUE && viewportHeight != Integer.MAX_VALUE) {
            int maxVideoPixelsToRetain = PriorityMap.PRIORITY_MAX;
            for (int i2 = 0; i2 < group.length; i2++) {
                Format format = group.getFormat(i2);
                if (format.width > 0 && format.height > 0) {
                    Point maxVideoSizeInViewport = getMaxVideoSizeInViewport(orientationMayChange, viewportWidth, viewportHeight, format.width, format.height);
                    int videoPixels = format.width * format.height;
                    if (format.width >= ((int) (maxVideoSizeInViewport.x * FRACTION_TO_CONSIDER_FULLSCREEN)) && format.height >= ((int) (maxVideoSizeInViewport.y * FRACTION_TO_CONSIDER_FULLSCREEN)) && videoPixels < maxVideoPixelsToRetain) {
                        maxVideoPixelsToRetain = videoPixels;
                    }
                }
            }
            if (maxVideoPixelsToRetain != Integer.MAX_VALUE) {
                for (int i3 = selectedTrackIndices.size() - 1; i3 >= 0; i3--) {
                    int pixelCount = group.getFormat(selectedTrackIndices.get(i3).intValue()).getPixelCount();
                    if (pixelCount == -1 || pixelCount > maxVideoPixelsToRetain) {
                        selectedTrackIndices.remove(i3);
                    }
                }
            }
        }
        return selectedTrackIndices;
    }

    private static Point getMaxVideoSizeInViewport(boolean orientationMayChange, int viewportWidth, int viewportHeight, int videoWidth, int videoHeight) {
        if (orientationMayChange) {
            if ((videoWidth > videoHeight) != (viewportWidth > viewportHeight)) {
                viewportWidth = viewportHeight;
                viewportHeight = viewportWidth;
            }
        }
        if (videoWidth * viewportHeight >= videoHeight * viewportWidth) {
            return new Point(viewportWidth, Util.ceilDivide(viewportWidth * videoHeight, videoWidth));
        }
        return new Point(Util.ceilDivide(viewportHeight * videoWidth, videoHeight), viewportHeight);
    }

    private static final class AudioConfigurationTuple {
        public final int channelCount;
        public final String mimeType;
        public final int sampleRate;

        public AudioConfigurationTuple(int channelCount, int sampleRate, String mimeType) {
            this.channelCount = channelCount;
            this.sampleRate = sampleRate;
            this.mimeType = mimeType;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            AudioConfigurationTuple other = (AudioConfigurationTuple) obj;
            return this.channelCount == other.channelCount && this.sampleRate == other.sampleRate && TextUtils.equals(this.mimeType, other.mimeType);
        }

        public int hashCode() {
            int result = this.channelCount;
            return (((result * 31) + this.sampleRate) * 31) + (this.mimeType != null ? this.mimeType.hashCode() : 0);
        }
    }
}
