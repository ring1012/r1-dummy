package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@ListenerClass(method = {@ListenerMethod(name = "onCheckedChanged", parameters = {"android.widget.CompoundButton", "boolean"})}, setter = "setOnCheckedChangeListener", targetType = "android.widget.CompoundButton", type = "android.widget.CompoundButton.OnCheckedChangeListener")
/* loaded from: classes.dex */
public @interface OnCheckedChanged {
    int[] value() default {-1};
}
