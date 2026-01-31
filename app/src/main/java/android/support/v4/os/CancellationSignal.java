package android.support.v4.os;

import android.os.Build;

/* loaded from: classes.dex */
public final class CancellationSignal {
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;

    public interface OnCancelListener {
        void onCancel();
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this) {
            z = this.mIsCanceled;
        }
        return z;
    }

    public void throwIfCanceled() {
        if (isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    public void cancel() {
        synchronized (this) {
            if (!this.mIsCanceled) {
                this.mIsCanceled = true;
                this.mCancelInProgress = true;
                OnCancelListener listener = this.mOnCancelListener;
                Object obj = this.mCancellationSignalObj;
                if (listener != null) {
                    try {
                        listener.onCancel();
                    } catch (Throwable th) {
                        synchronized (this) {
                            this.mCancelInProgress = false;
                            notifyAll();
                            throw th;
                        }
                    }
                }
                if (obj != null) {
                    CancellationSignalCompatJellybean.cancel(obj);
                }
                synchronized (this) {
                    this.mCancelInProgress = false;
                    notifyAll();
                }
            }
        }
    }

    public void setOnCancelListener(OnCancelListener listener) {
        synchronized (this) {
            waitForCancelFinishedLocked();
            if (this.mOnCancelListener != listener) {
                this.mOnCancelListener = listener;
                if (this.mIsCanceled && listener != null) {
                    listener.onCancel();
                }
            }
        }
    }

    public Object getCancellationSignalObject() {
        Object obj;
        if (Build.VERSION.SDK_INT < 16) {
            return null;
        }
        synchronized (this) {
            if (this.mCancellationSignalObj == null) {
                this.mCancellationSignalObj = CancellationSignalCompatJellybean.create();
                if (this.mIsCanceled) {
                    CancellationSignalCompatJellybean.cancel(this.mCancellationSignalObj);
                }
            }
            obj = this.mCancellationSignalObj;
        }
        return obj;
    }

    private void waitForCancelFinishedLocked() throws InterruptedException {
        while (this.mCancelInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
}
