package android.support.v4.os;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.support.annotation.RequiresApi;

@RequiresApi(11)
@TargetApi(11)
/* loaded from: classes.dex */
class AsyncTaskCompatHoneycomb {
    AsyncTaskCompatHoneycomb() {
    }

    static <Params, Progress, Result> void executeParallel(AsyncTask<Params, Progress, Result> task, Params... params) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }
}
