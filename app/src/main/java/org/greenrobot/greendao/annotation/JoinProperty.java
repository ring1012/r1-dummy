package org.greenrobot.greendao.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: classes.dex */
public @interface JoinProperty {
    String name();

    String referencedName();
}
