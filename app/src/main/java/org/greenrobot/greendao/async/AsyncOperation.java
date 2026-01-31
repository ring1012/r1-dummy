package org.greenrobot.greendao.async;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.database.Database;

/* loaded from: classes.dex */
public class AsyncOperation {
    public static final int FLAG_MERGE_TX = 1;
    public static final int FLAG_STOP_QUEUE_ON_EXCEPTION = 2;
    public static final int FLAG_TRACK_CREATOR_STACKTRACE = 4;
    private volatile boolean completed;
    final Exception creatorStacktrace;
    final AbstractDao<Object, Object> dao;
    private final Database database;
    final int flags;
    volatile int mergedOperationsCount;
    final Object parameter;
    volatile Object result;
    int sequenceNumber;
    volatile Throwable throwable;
    volatile long timeCompleted;
    volatile long timeStarted;
    final OperationType type;

    public enum OperationType {
        Insert,
        InsertInTxIterable,
        InsertInTxArray,
        InsertOrReplace,
        InsertOrReplaceInTxIterable,
        InsertOrReplaceInTxArray,
        Update,
        UpdateInTxIterable,
        UpdateInTxArray,
        Delete,
        DeleteInTxIterable,
        DeleteInTxArray,
        DeleteByKey,
        DeleteAll,
        TransactionRunnable,
        TransactionCallable,
        QueryList,
        QueryUnique,
        Load,
        LoadAll,
        Count,
        Refresh
    }

    AsyncOperation(OperationType type, AbstractDao<?, ?> dao, Database database, Object parameter, int flags) {
        this.type = type;
        this.flags = flags;
        this.dao = dao;
        this.database = database;
        this.parameter = parameter;
        this.creatorStacktrace = (flags & 4) != 0 ? new Exception("AsyncOperation was created here") : null;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public OperationType getType() {
        return this.type;
    }

    public Object getParameter() {
        return this.parameter;
    }

    public synchronized Object getResult() {
        if (!this.completed) {
            waitForCompletion();
        }
        if (this.throwable != null) {
            throw new AsyncDaoException(this, this.throwable);
        }
        return this.result;
    }

    public boolean isMergeTx() {
        return (this.flags & 1) != 0;
    }

    Database getDatabase() {
        return this.database != null ? this.database : this.dao.getDatabase();
    }

    boolean isMergeableWith(AsyncOperation other) {
        return other != null && isMergeTx() && other.isMergeTx() && getDatabase() == other.getDatabase();
    }

    public long getTimeStarted() {
        return this.timeStarted;
    }

    public long getTimeCompleted() {
        return this.timeCompleted;
    }

    public long getDuration() {
        if (this.timeCompleted == 0) {
            throw new DaoException("This operation did not yet complete");
        }
        return this.timeCompleted - this.timeStarted;
    }

    public boolean isFailed() {
        return this.throwable != null;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public synchronized Object waitForCompletion() {
        while (!this.completed) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for operation to complete", e);
            }
        }
        return this.result;
    }

    public synchronized boolean waitForCompletion(int maxMillis) {
        if (!this.completed) {
            try {
                wait(maxMillis);
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for operation to complete", e);
            }
        }
        return this.completed;
    }

    synchronized void setCompleted() {
        this.completed = true;
        notifyAll();
    }

    public boolean isCompletedSucessfully() {
        return this.completed && this.throwable == null;
    }

    public int getMergedOperationsCount() {
        return this.mergedOperationsCount;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    void reset() {
        this.timeStarted = 0L;
        this.timeCompleted = 0L;
        this.completed = false;
        this.throwable = null;
        this.result = null;
        this.mergedOperationsCount = 0;
    }

    public Exception getCreatorStacktrace() {
        return this.creatorStacktrace;
    }
}
