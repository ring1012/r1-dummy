package org.greenrobot.greendao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: classes.dex */
public @interface Entity {
    boolean active() default false;

    boolean createInDb() default true;

    Index[] indexes() default {};

    String nameInDb() default "";

    String schema() default "default";
}
