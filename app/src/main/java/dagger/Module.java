package dagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes.dex */
public @interface Module {
    Class<?> addsTo() default Void.class;

    boolean complete() default true;

    Class<?>[] includes() default {};

    Class<?>[] injects() default {};

    boolean library() default false;

    boolean overrides() default false;

    Class<?>[] staticInjections() default {};
}
