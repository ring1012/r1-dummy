package rx.internal.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.SerialSubscription;

/* loaded from: classes.dex */
public final class OnSubscribeJoin<TLeft, TRight, TLeftDuration, TRightDuration, R> implements Observable.OnSubscribe<R> {
    final Observable<TLeft> left;
    final Func1<TLeft, Observable<TLeftDuration>> leftDurationSelector;
    final Func2<TLeft, TRight, R> resultSelector;
    final Observable<TRight> right;
    final Func1<TRight, Observable<TRightDuration>> rightDurationSelector;

    public OnSubscribeJoin(Observable<TLeft> left, Observable<TRight> right, Func1<TLeft, Observable<TLeftDuration>> leftDurationSelector, Func1<TRight, Observable<TRightDuration>> rightDurationSelector, Func2<TLeft, TRight, R> resultSelector) {
        this.left = left;
        this.right = right;
        this.leftDurationSelector = leftDurationSelector;
        this.rightDurationSelector = rightDurationSelector;
        this.resultSelector = resultSelector;
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super R> t1) {
        OnSubscribeJoin<TLeft, TRight, TLeftDuration, TRightDuration, R>.ResultSink result = new ResultSink(new SerializedSubscriber(t1));
        result.run();
    }

    final class ResultSink {
        boolean leftDone;
        int leftId;
        boolean rightDone;
        int rightId;
        final Subscriber<? super R> subscriber;
        final Object guard = new Object();
        final CompositeSubscription group = new CompositeSubscription();
        final Map<Integer, TLeft> leftMap = new HashMap();
        final Map<Integer, TRight> rightMap = new HashMap();

        public ResultSink(Subscriber<? super R> subscriber) {
            this.subscriber = subscriber;
        }

        public void run() {
            this.subscriber.add(this.group);
            LeftSubscriber leftSubscriber = new LeftSubscriber();
            RightSubscriber rightSubscriber = new RightSubscriber();
            this.group.add(leftSubscriber);
            this.group.add(rightSubscriber);
            OnSubscribeJoin.this.left.unsafeSubscribe(leftSubscriber);
            OnSubscribeJoin.this.right.unsafeSubscribe(rightSubscriber);
        }

        final class LeftSubscriber extends Subscriber<TLeft> {
            LeftSubscriber() {
            }

            protected void expire(int id, Subscription resource) throws Throwable {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    if (ResultSink.this.leftMap.remove(Integer.valueOf(id)) != null && ResultSink.this.leftMap.isEmpty() && ResultSink.this.leftDone) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                } else {
                    ResultSink.this.group.remove(resource);
                }
            }

            @Override // rx.Observer
            public void onNext(TLeft tleft) {
                int i;
                int i2;
                synchronized (ResultSink.this.guard) {
                    ResultSink resultSink = ResultSink.this;
                    i = resultSink.leftId;
                    resultSink.leftId = i + 1;
                    ResultSink.this.leftMap.put(Integer.valueOf(i), tleft);
                    i2 = ResultSink.this.rightId;
                }
                try {
                    Observable<TLeftDuration> observableCall = OnSubscribeJoin.this.leftDurationSelector.call(tleft);
                    LeftDurationSubscriber leftDurationSubscriber = new LeftDurationSubscriber(i);
                    ResultSink.this.group.add(leftDurationSubscriber);
                    observableCall.unsafeSubscribe(leftDurationSubscriber);
                    ArrayList arrayList = new ArrayList();
                    synchronized (ResultSink.this.guard) {
                        for (Map.Entry<Integer, TRight> entry : ResultSink.this.rightMap.entrySet()) {
                            if (entry.getKey().intValue() < i2) {
                                arrayList.add(entry.getValue());
                            }
                        }
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ResultSink.this.subscriber.onNext(OnSubscribeJoin.this.resultSelector.call(tleft, it.next()));
                    }
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, this);
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) throws Throwable {
                ResultSink.this.subscriber.onError(e);
                ResultSink.this.subscriber.unsubscribe();
            }

            @Override // rx.Observer
            public void onCompleted() throws Throwable {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    ResultSink.this.leftDone = true;
                    if (ResultSink.this.rightDone || ResultSink.this.leftMap.isEmpty()) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                } else {
                    ResultSink.this.group.remove(this);
                }
            }

            final class LeftDurationSubscriber extends Subscriber<TLeftDuration> {
                final int id;
                boolean once = true;

                public LeftDurationSubscriber(int id) {
                    this.id = id;
                }

                @Override // rx.Observer
                public void onNext(TLeftDuration args) throws Throwable {
                    onCompleted();
                }

                @Override // rx.Observer
                public void onError(Throwable e) throws Throwable {
                    LeftSubscriber.this.onError(e);
                }

                @Override // rx.Observer
                public void onCompleted() throws Throwable {
                    if (this.once) {
                        this.once = false;
                        LeftSubscriber.this.expire(this.id, this);
                    }
                }
            }
        }

        final class RightSubscriber extends Subscriber<TRight> {
            RightSubscriber() {
            }

            void expire(int id, Subscription resource) throws Throwable {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    if (ResultSink.this.rightMap.remove(Integer.valueOf(id)) != null && ResultSink.this.rightMap.isEmpty() && ResultSink.this.rightDone) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                } else {
                    ResultSink.this.group.remove(resource);
                }
            }

            @Override // rx.Observer
            public void onNext(TRight tright) {
                int i;
                int i2;
                synchronized (ResultSink.this.guard) {
                    ResultSink resultSink = ResultSink.this;
                    i = resultSink.rightId;
                    resultSink.rightId = i + 1;
                    ResultSink.this.rightMap.put(Integer.valueOf(i), tright);
                    i2 = ResultSink.this.leftId;
                }
                ResultSink.this.group.add(new SerialSubscription());
                try {
                    Observable<TRightDuration> observableCall = OnSubscribeJoin.this.rightDurationSelector.call(tright);
                    RightDurationSubscriber rightDurationSubscriber = new RightDurationSubscriber(i);
                    ResultSink.this.group.add(rightDurationSubscriber);
                    observableCall.unsafeSubscribe(rightDurationSubscriber);
                    ArrayList arrayList = new ArrayList();
                    synchronized (ResultSink.this.guard) {
                        for (Map.Entry<Integer, TLeft> entry : ResultSink.this.leftMap.entrySet()) {
                            if (entry.getKey().intValue() < i2) {
                                arrayList.add(entry.getValue());
                            }
                        }
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ResultSink.this.subscriber.onNext(OnSubscribeJoin.this.resultSelector.call(it.next(), tright));
                    }
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, this);
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) throws Throwable {
                ResultSink.this.subscriber.onError(e);
                ResultSink.this.subscriber.unsubscribe();
            }

            @Override // rx.Observer
            public void onCompleted() throws Throwable {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    ResultSink.this.rightDone = true;
                    if (ResultSink.this.leftDone || ResultSink.this.rightMap.isEmpty()) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                } else {
                    ResultSink.this.group.remove(this);
                }
            }

            final class RightDurationSubscriber extends Subscriber<TRightDuration> {
                final int id;
                boolean once = true;

                public RightDurationSubscriber(int id) {
                    this.id = id;
                }

                @Override // rx.Observer
                public void onNext(TRightDuration args) throws Throwable {
                    onCompleted();
                }

                @Override // rx.Observer
                public void onError(Throwable e) throws Throwable {
                    RightSubscriber.this.onError(e);
                }

                @Override // rx.Observer
                public void onCompleted() throws Throwable {
                    if (this.once) {
                        this.once = false;
                        RightSubscriber.this.expire(this.id, this);
                    }
                }
            }
        }
    }
}
