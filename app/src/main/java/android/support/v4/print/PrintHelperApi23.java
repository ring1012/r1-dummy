package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.Context;
import android.print.PrintAttributes;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
@TargetApi(23)
/* loaded from: classes.dex */
class PrintHelperApi23 extends PrintHelperApi20 {
    @Override // android.support.v4.print.PrintHelperKitkat
    protected PrintAttributes.Builder copyAttributes(PrintAttributes other) {
        PrintAttributes.Builder b = super.copyAttributes(other);
        if (other.getDuplexMode() != 0) {
            b.setDuplexMode(other.getDuplexMode());
        }
        return b;
    }

    PrintHelperApi23(Context context) {
        super(context);
        this.mIsMinMarginsHandlingCorrect = false;
    }
}
