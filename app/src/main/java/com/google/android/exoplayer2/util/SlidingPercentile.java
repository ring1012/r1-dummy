package com.google.android.exoplayer2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* loaded from: classes.dex */
public class SlidingPercentile {
    private static final int MAX_RECYCLED_SAMPLES = 5;
    private static final int SORT_ORDER_BY_INDEX = 1;
    private static final int SORT_ORDER_BY_VALUE = 0;
    private static final int SORT_ORDER_NONE = -1;
    private final int maxWeight;
    private int nextSampleIndex;
    private int recycledSampleCount;
    private int totalWeight;
    private static final Comparator<Sample> INDEX_COMPARATOR = new Comparator<Sample>() { // from class: com.google.android.exoplayer2.util.SlidingPercentile.1
        @Override // java.util.Comparator
        public int compare(Sample a2, Sample b) {
            return a2.index - b.index;
        }
    };
    private static final Comparator<Sample> VALUE_COMPARATOR = new Comparator<Sample>() { // from class: com.google.android.exoplayer2.util.SlidingPercentile.2
        @Override // java.util.Comparator
        public int compare(Sample a2, Sample b) {
            if (a2.value < b.value) {
                return -1;
            }
            return b.value < a2.value ? 1 : 0;
        }
    };
    private final Sample[] recycledSamples = new Sample[5];
    private final ArrayList<Sample> samples = new ArrayList<>();
    private int currentSortOrder = -1;

    public SlidingPercentile(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void addSample(int weight, float value) {
        Sample newSample;
        ensureSortedByIndex();
        if (this.recycledSampleCount > 0) {
            Sample[] sampleArr = this.recycledSamples;
            int i = this.recycledSampleCount - 1;
            this.recycledSampleCount = i;
            newSample = sampleArr[i];
        } else {
            newSample = new Sample();
        }
        int i2 = this.nextSampleIndex;
        this.nextSampleIndex = i2 + 1;
        newSample.index = i2;
        newSample.weight = weight;
        newSample.value = value;
        this.samples.add(newSample);
        this.totalWeight += weight;
        while (this.totalWeight > this.maxWeight) {
            int excessWeight = this.totalWeight - this.maxWeight;
            Sample oldestSample = this.samples.get(0);
            if (oldestSample.weight <= excessWeight) {
                this.totalWeight -= oldestSample.weight;
                this.samples.remove(0);
                if (this.recycledSampleCount < 5) {
                    Sample[] sampleArr2 = this.recycledSamples;
                    int i3 = this.recycledSampleCount;
                    this.recycledSampleCount = i3 + 1;
                    sampleArr2[i3] = oldestSample;
                }
            } else {
                oldestSample.weight -= excessWeight;
                this.totalWeight -= excessWeight;
            }
        }
    }

    public float getPercentile(float percentile) {
        ensureSortedByValue();
        float desiredWeight = percentile * this.totalWeight;
        int accumulatedWeight = 0;
        for (int i = 0; i < this.samples.size(); i++) {
            Sample currentSample = this.samples.get(i);
            accumulatedWeight += currentSample.weight;
            if (accumulatedWeight >= desiredWeight) {
                return currentSample.value;
            }
        }
        if (this.samples.isEmpty()) {
            return Float.NaN;
        }
        return this.samples.get(this.samples.size() - 1).value;
    }

    private void ensureSortedByIndex() {
        if (this.currentSortOrder != 1) {
            Collections.sort(this.samples, INDEX_COMPARATOR);
            this.currentSortOrder = 1;
        }
    }

    private void ensureSortedByValue() {
        if (this.currentSortOrder != 0) {
            Collections.sort(this.samples, VALUE_COMPARATOR);
            this.currentSortOrder = 0;
        }
    }

    private static class Sample {
        public int index;
        public float value;
        public int weight;

        private Sample() {
        }
    }
}
