package com.google.android.exoplayer2.source;

/* loaded from: classes.dex */
public final class CompositeSequenceableLoader implements SequenceableLoader {
    private final SequenceableLoader[] loaders;

    public CompositeSequenceableLoader(SequenceableLoader[] loaders) {
        this.loaders = loaders;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        long nextLoadPositionUs = Long.MAX_VALUE;
        for (SequenceableLoader loader : this.loaders) {
            long loaderNextLoadPositionUs = loader.getNextLoadPositionUs();
            if (loaderNextLoadPositionUs != Long.MIN_VALUE) {
                nextLoadPositionUs = Math.min(nextLoadPositionUs, loaderNextLoadPositionUs);
            }
        }
        if (nextLoadPositionUs == Long.MAX_VALUE) {
            return Long.MIN_VALUE;
        }
        return nextLoadPositionUs;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long positionUs) {
        boolean madeProgressThisIteration;
        boolean madeProgress = false;
        do {
            madeProgressThisIteration = false;
            long nextLoadPositionUs = getNextLoadPositionUs();
            if (nextLoadPositionUs == Long.MIN_VALUE) {
                break;
            }
            for (SequenceableLoader loader : this.loaders) {
                if (loader.getNextLoadPositionUs() == nextLoadPositionUs) {
                    madeProgressThisIteration |= loader.continueLoading(positionUs);
                }
            }
            madeProgress |= madeProgressThisIteration;
        } while (madeProgressThisIteration);
        return madeProgress;
    }
}
