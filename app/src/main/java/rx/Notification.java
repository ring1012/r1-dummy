package rx;

import cn.yunzhisheng.common.PinyinConverter;

/* loaded from: classes.dex */
public final class Notification<T> {
    private static final Notification<Void> ON_COMPLETED = new Notification<>(Kind.OnCompleted, null, null);
    private final Kind kind;
    private final Throwable throwable;
    private final T value;

    public enum Kind {
        OnNext,
        OnError,
        OnCompleted
    }

    public static <T> Notification<T> createOnNext(T t) {
        return new Notification<>(Kind.OnNext, t, null);
    }

    public static <T> Notification<T> createOnError(Throwable e) {
        return new Notification<>(Kind.OnError, null, e);
    }

    public static <T> Notification<T> createOnCompleted() {
        return (Notification<T>) ON_COMPLETED;
    }

    public static <T> Notification<T> createOnCompleted(Class<T> cls) {
        return (Notification<T>) ON_COMPLETED;
    }

    private Notification(Kind kind, T value, Throwable e) {
        this.value = value;
        this.throwable = e;
        this.kind = kind;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public T getValue() {
        return this.value;
    }

    public boolean hasValue() {
        return isOnNext() && this.value != null;
    }

    public boolean hasThrowable() {
        return isOnError() && this.throwable != null;
    }

    public Kind getKind() {
        return this.kind;
    }

    public boolean isOnError() {
        return getKind() == Kind.OnError;
    }

    public boolean isOnCompleted() {
        return getKind() == Kind.OnCompleted;
    }

    public boolean isOnNext() {
        return getKind() == Kind.OnNext;
    }

    public void accept(Observer<? super T> observer) {
        if (isOnNext()) {
            observer.onNext(getValue());
        } else if (isOnCompleted()) {
            observer.onCompleted();
        } else if (isOnError()) {
            observer.onError(getThrowable());
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder("[").append(super.toString()).append(PinyinConverter.PINYIN_SEPARATOR).append(getKind());
        if (hasValue()) {
            str.append(PinyinConverter.PINYIN_SEPARATOR).append(getValue());
        }
        if (hasThrowable()) {
            str.append(PinyinConverter.PINYIN_SEPARATOR).append(getThrowable().getMessage());
        }
        str.append("]");
        return str.toString();
    }

    public int hashCode() {
        int hash = getKind().hashCode();
        if (hasValue()) {
            hash = (hash * 31) + getValue().hashCode();
        }
        if (hasThrowable()) {
            return (hash * 31) + getThrowable().hashCode();
        }
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Notification<?> notification = (Notification) obj;
        if (notification.getKind() != getKind()) {
            return false;
        }
        if (hasValue() && !getValue().equals(notification.getValue())) {
            return false;
        }
        if (hasThrowable() && !getThrowable().equals(notification.getThrowable())) {
            return false;
        }
        if (hasValue() || hasThrowable() || !notification.hasValue()) {
            return hasValue() || hasThrowable() || !notification.hasThrowable();
        }
        return false;
    }
}
