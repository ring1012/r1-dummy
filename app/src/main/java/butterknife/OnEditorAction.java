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
@ListenerClass(method = {@ListenerMethod(defaultReturn = Bugly.SDK_IS_DEV, name = "onEditorAction", parameters = {"android.widget.TextView", "int", "android.view.KeyEvent"}, returnType = "boolean")}, setter = "setOnEditorActionListener", targetType = "android.widget.TextView", type = "android.widget.TextView.OnEditorActionListener")
/* loaded from: classes.dex */
public @interface OnEditorAction {
    int[] value() default {-1};
}
