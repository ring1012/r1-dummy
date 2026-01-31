package android.support.v4.graphics;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;

@RequiresApi(12)
@TargetApi(12)
/* loaded from: classes.dex */
class BitmapCompatHoneycombMr1 {
    BitmapCompatHoneycombMr1() {
    }

    static int getAllocationByteCount(Bitmap bitmap) {
        return bitmap.getByteCount();
    }
}
