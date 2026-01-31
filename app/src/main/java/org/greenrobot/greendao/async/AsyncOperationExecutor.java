package org.greenrobot.greendao.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

/* loaded from: classes.dex */
class AsyncOperationExecutor implements Runnable, Handler.Callback {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private int countOperationsCompleted;
    private int countOperationsEnqueued;
    private volatile boolean executorRunning;
    private Handler handlerMainThread;
    private int lastSequenceNumber;
    private volatile AsyncOperationListener listener;
    private volatile AsyncOperationListener listenerMainThread;
    private final BlockingQueue<AsyncOperation> queue = new LinkedBlockingQueue();
    private volatile int maxOperationCountToMerge = 50;
    private volatile int waitForMergeMillis = 50;

    AsyncOperationExecutor() {
    }

    public void enqueue(AsyncOperation operation) {
        synchronized (this) {
            int i = this.lastSequenceNumber + 1;
            this.lastSequenceNumber = i;
            operation.sequenceNumber = i;
            this.queue.add(operation);
            this.countOperationsEnqueued++;
            if (!this.executorRunning) {
                this.executorRunning = true;
                executorService.execute(this);
            }
        }
    }

    public int getMaxOperationCountToMerge() {
        return this.maxOperationCountToMerge;
    }

    public void setMaxOperationCountToMerge(int maxOperationCountToMerge) {
        this.maxOperationCountToMerge = maxOperationCountToMerge;
    }

    public int getWaitForMergeMillis() {
        return this.waitForMergeMillis;
    }

    public void setWaitForMergeMillis(int waitForMergeMillis) {
        this.waitForMergeMillis = waitForMergeMillis;
    }

    public AsyncOperationListener getListener() {
        return this.listener;
    }

    public void setListener(AsyncOperationListener listener) {
        this.listener = listener;
    }

    public AsyncOperationListener getListenerMainThread() {
        return this.listenerMainThread;
    }

    public void setListenerMainThread(AsyncOperationListener listenerMainThread) {
        this.listenerMainThread = listenerMainThread;
    }

    public synchronized boolean isCompleted() {
        return this.countOperationsEnqueued == this.countOperationsCompleted;
    }

    public synchronized void waitForCompletion() {
        while (!isCompleted()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for all operations to complete", e);
            }
        }
    }

    public synchronized boolean waitForCompletion(int maxMillis) {
        if (!isCompleted()) {
            try {
                wait(maxMillis);
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for all operations to complete", e);
            }
        }
        return isCompleted();
    }

    @Override // java.lang.Runnable
    public void run() {
        AsyncOperation operation2;
        while (true) {
            try {
                AsyncOperation operation = this.queue.poll(1L, TimeUnit.SECONDS);
                if (operation == null) {
                    synchronized (this) {
                        operation = this.queue.poll();
                        if (operation == null) {
                            this.executorRunning = false;
                            return;
                        }
                    }
                }
                if (operation.isMergeTx() && (operation2 = this.queue.poll(this.waitForMergeMillis, TimeUnit.MILLISECONDS)) != null) {
                    if (operation.isMergeableWith(operation2)) {
                        mergeTxAndExecute(operation, operation2);
                    } else {
                        executeOperationAndPostCompleted(operation);
                        executeOperationAndPostCompleted(operation2);
                    }
                } else {
                    executeOperationAndPostCompleted(operation);
                }
            } catch (InterruptedException e) {
                DaoLog.w(Thread.currentThread().getName() + " was interruppted", e);
                return;
            } finally {
                this.executorRunning = false;
            }
        }
    }

    private void mergeTxAndExecute(AsyncOperation operation1, AsyncOperation operation2) {
        ArrayList<AsyncOperation> mergedOps = new ArrayList<>();
        mergedOps.add(operation1);
        mergedOps.add(operation2);
        Database db = operation1.getDatabase();
        db.beginTransaction();
        boolean success = false;
        for (int i = 0; i < mergedOps.size(); i++) {
            try {
                AsyncOperation operation = mergedOps.get(i);
                executeOperation(operation);
                if (!operation.isFailed()) {
                    if (i == mergedOps.size() - 1) {
                        AsyncOperation peekedOp = this.queue.peek();
                        if (i < this.maxOperationCountToMerge && operation.isMergeableWith(peekedOp)) {
                            AsyncOperation removedOp = this.queue.remove();
                            if (removedOp != peekedOp) {
                                throw new DaoException("Internal error: peeked op did not match removed op");
                            }
                            mergedOps.add(removedOp);
                        } else {
                            db.setTransactionSuccessful();
                            success = true;
                            break;
                        }
                    }
                }
            } finally {
                try {
                    db.endTransaction();
                } catch (RuntimeException e) {
                    DaoLog.i("Async transaction could not be ended, success so far was: false", e);
                }
            }
        }
        if (success) {
            int mergedCount = mergedOps.size();
            Iterator<AsyncOperation> it = mergedOps.iterator();
            while (it.hasNext()) {
                AsyncOperation asyncOperation = it.next();
                asyncOperation.mergedOperationsCount = mergedCount;
                handleOperationCompleted(asyncOperation);
            }
            return;
        }
        DaoLog.i("Reverted merged transaction because one of the operations failed. Executing operations one by one instead...");
        Iterator<AsyncOperation> it2 = mergedOps.iterator();
        while (it2.hasNext()) {
            AsyncOperation asyncOperation2 = it2.next();
            asyncOperation2.reset();
            executeOperationAndPostCompleted(asyncOperation2);
        }
    }

    private void handleOperationCompleted(AsyncOperation operation) {
        operation.setCompleted();
        AsyncOperationListener listenerToCall = this.listener;
        if (listenerToCall != null) {
            listenerToCall.onAsyncOperationCompleted(operation);
        }
        if (this.listenerMainThread != null) {
            if (this.handlerMainThread == null) {
                this.handlerMainThread = new Handler(Looper.getMainLooper(), this);
            }
            Message msg = this.handlerMainThread.obtainMessage(1, operation);
            this.handlerMainThread.sendMessage(msg);
        }
        synchronized (this) {
            this.countOperationsCompleted++;
            if (this.countOperationsCompleted == this.countOperationsEnqueued) {
                notifyAll();
            }
        }
    }

    private void executeOperationAndPostCompleted(AsyncOperation operation) {
        executeOperation(operation);
        handleOperationCompleted(operation);
    }

    private void executeOperation(AsyncOperation operation) {
        operation.timeStarted = System.currentTimeMillis();
        try {
            switch (operation.type) {
                case Delete:
                    operation.dao.delete(operation.parameter);
                    break;
                case DeleteInTxIterable:
                    operation.dao.deleteInTx((Iterable<Object>) operation.parameter);
                    break;
                case DeleteInTxArray:
                    operation.dao.deleteInTx((Object[]) operation.parameter);
                    break;
                case Insert:
                    operation.dao.insert(operation.parameter);
                    break;
                case InsertInTxIterable:
                    operation.dao.insertInTx((Iterable<Object>) operation.parameter);
                    break;
                case InsertInTxArray:
                    operation.dao.insertInTx((Object[]) operation.parameter);
                    break;
                case InsertOrReplace:
                    operation.dao.insertOrReplace(operation.parameter);
                    break;
                case InsertOrReplaceInTxIterable:
                    operation.dao.insertOrReplaceInTx((Iterable<Object>) operation.parameter);
                    break;
                case InsertOrReplaceInTxArray:
                    operation.dao.insertOrReplaceInTx((Object[]) operation.parameter);
                    break;
                case Update:
                    operation.dao.update(operation.parameter);
                    break;
                case UpdateInTxIterable:
                    operation.dao.updateInTx((Iterable<Object>) operation.parameter);
                    break;
                case UpdateInTxArray:
                    operation.dao.updateInTx((Object[]) operation.parameter);
                    break;
                case TransactionRunnable:
                    executeTransactionRunnable(operation);
                    break;
                case TransactionCallable:
                    executeTransactionCallable(operation);
                    break;
                case QueryList:
                    operation.result = ((Query) operation.parameter).forCurrentThread().list();
                    break;
                case QueryUnique:
                    operation.result = ((Query) operation.parameter).forCurrentThread().unique();
                    break;
                case DeleteByKey:
                    operation.dao.deleteByKey(operation.parameter);
                    break;
                case DeleteAll:
                    operation.dao.deleteAll();
                    break;
                case Load:
                    operation.result = operation.dao.load(operation.parameter);
                    break;
                case LoadAll:
                    operation.result = operation.dao.loadAll();
                    break;
                case Count:
                    operation.result = Long.valueOf(operation.dao.count());
                    break;
                case Refresh:
                    operation.dao.refresh(operation.parameter);
                    break;
                default:
                    throw new DaoException("Unsupported operation: " + operation.type);
            }
        } catch (Throwable th) {
            operation.throwable = th;
        }
        operation.timeCompleted = System.currentTimeMillis();
    }

    private void executeTransactionRunnable(AsyncOperation operation) {
        Database db = operation.getDatabase();
        db.beginTransaction();
        try {
            ((Runnable) operation.parameter).run();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void executeTransactionCallable(AsyncOperation operation) throws Exception {
        Database db = operation.getDatabase();
        db.beginTransaction();
        try {
            operation.result = ((Callable) operation.parameter).call();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        AsyncOperationListener listenerToCall = this.listenerMainThread;
        if (listenerToCall != null) {
            listenerToCall.onAsyncOperationCompleted((AsyncOperation) msg.obj);
            return false;
        }
        return false;
    }
}
