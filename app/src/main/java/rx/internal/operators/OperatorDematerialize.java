package rx.internal.operators;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;

/* loaded from: classes.dex */
public final class OperatorDematerialize<T> implements Observable.Operator<T, Notification<T>> {

    private static final class Holder {
        static final OperatorDematerialize<Object> INSTANCE = new OperatorDematerialize<>();

        private Holder() {
        }
    }

    public static OperatorDematerialize instance() {
        return Holder.INSTANCE;
    }

    private OperatorDematerialize() {
    }

    @Override // rx.functions.Func1
    public Subscriber<? super Notification<T>> call(final Subscriber<? super T> child) {
        return new Subscriber<Notification<T>>(child) { // from class: rx.internal.operators.OperatorDematerialize.1
            boolean terminated;

            @Override // rx.Observer
            public void onNext(Notification<T> t) {
                switch (AnonymousClass2.$SwitchMap$rx$Notification$Kind[t.getKind().ordinal()]) {
                    case 1:
                        if (!this.terminated) {
                            child.onNext(t.getValue());
                            break;
                        }
                        break;
                    case 2:
                        onError(t.getThrowable());
                        break;
                    case 3:
                        onCompleted();
                        break;
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                if (!this.terminated) {
                    this.terminated = true;
                    child.onError(e);
                }
            }

            @Override // rx.Observer
            public void onCompleted() {
                if (!this.terminated) {
                    this.terminated = true;
                    child.onCompleted();
                }
            }
        };
    }

    /* renamed from: rx.internal.operators.OperatorDematerialize$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$rx$Notification$Kind = new int[Notification.Kind.values().length];

        static {
            try {
                $SwitchMap$rx$Notification$Kind[Notification.Kind.OnNext.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Notification.Kind.OnError.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Notification.Kind.OnCompleted.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }
}
