package org.litepal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes.dex */
public @interface Column {
    String defaultValue() default "";

    boolean ignore() default false;

    boolean nullable() default true;

    boolean unique() default false;
}
