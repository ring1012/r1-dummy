package org.greenrobot.greendao.converter;

/* loaded from: classes.dex */
public interface PropertyConverter<P, D> {
    D convertToDatabaseValue(P p);

    P convertToEntityProperty(D d);
}
