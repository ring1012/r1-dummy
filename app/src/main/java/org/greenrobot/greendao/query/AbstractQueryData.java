package org.greenrobot.greendao.query;

import android.os.Process;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.AbstractQuery;

/* loaded from: classes.dex */
abstract class AbstractQueryData<T, Q extends AbstractQuery<T>> {
    final AbstractDao<T, ?> dao;
    final String[] initialValues;
    final SparseArray<WeakReference<Q>> queriesForThreads = new SparseArray<>();
    final String sql;

    protected abstract Q createQuery();

    AbstractQueryData(AbstractDao<T, ?> dao, String sql, String[] initialValues) {
        this.dao = dao;
        this.sql = sql;
        this.initialValues = initialValues;
    }

    Q forCurrentThread(Q q) {
        if (Thread.currentThread() != q.ownerThread) {
            return (Q) forCurrentThread();
        }
        System.arraycopy(this.initialValues, 0, q.parameters, 0, this.initialValues.length);
        return q;
    }

    Q forCurrentThread() {
        Q q;
        int iMyTid = Process.myTid();
        if (iMyTid == 0) {
            long id = Thread.currentThread().getId();
            if (id < 0 || id > 2147483647L) {
                throw new RuntimeException("Cannot handle thread ID: " + id);
            }
            iMyTid = (int) id;
        }
        synchronized (this.queriesForThreads) {
            WeakReference<Q> weakReference = this.queriesForThreads.get(iMyTid);
            q = weakReference != null ? weakReference.get() : null;
            if (q == null) {
                gc();
                q = (Q) createQuery();
                this.queriesForThreads.put(iMyTid, new WeakReference<>(q));
            } else {
                System.arraycopy(this.initialValues, 0, q.parameters, 0, this.initialValues.length);
            }
        }
        return q;
    }

    void gc() {
        synchronized (this.queriesForThreads) {
            for (int i = this.queriesForThreads.size() - 1; i >= 0; i--) {
                if (this.queriesForThreads.valueAt(i).get() == null) {
                    this.queriesForThreads.remove(this.queriesForThreads.keyAt(i));
                }
            }
        }
    }
}
