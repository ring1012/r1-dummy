package org.greenrobot.greendao.async;

import org.greenrobot.greendao.DaoException;

/* loaded from: classes.dex */
public class AsyncDaoException extends DaoException {
    private static final long serialVersionUID = 5872157552005102382L;
    private final AsyncOperation failedOperation;

    public AsyncDaoException(AsyncOperation failedOperation, Throwable cause) {
        super(cause);
        this.failedOperation = failedOperation;
    }

    public AsyncOperation getFailedOperation() {
        return this.failedOperation;
    }
}
