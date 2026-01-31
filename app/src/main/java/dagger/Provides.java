package dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes.dex */
public @interface Provides {

    public enum Type {
        UNIQUE,
        SET,
        SET_VALUES
    }

    Type type() default Type.UNIQUE;
}
