package android.support.v4.graphics;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;

@RequiresApi(18)
@TargetApi(18)
/* loaded from: classes.dex */
class BitmapCompatJellybeanMR2 {
    BitmapCompatJellybeanMR2() {
    }

    public static boolean hasMipMap(Bitmap bitmap) {
        return bitmap.hasMipMap();
    }

    public static void setHasMipMap(Bitmap bitmap, boolean hasMipMap) {
        bitmap.setHasMipMap(hasMipMap);
    }
}
