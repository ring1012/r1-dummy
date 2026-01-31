package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import com.tencent.bugly.Bugly;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@ListenerClass(method = {@ListenerMethod(defaultReturn = Bugly.SDK_IS_DEV, name = "onLongClick", parameters = {"android.view.View"}, returnType = "boolean")}, setter = "setOnLongClickListener", targetType = "android.view.View", type = "android.view.View.OnLongClickListener")
/* loaded from: classes.dex */
public @interface OnLongClick {
    int[] value() default {-1};
}
