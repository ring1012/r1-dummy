package rx;

import com.unisound.vui.priority.PriorityMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import rx.annotations.Beta;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.functions.Func7;
import rx.functions.Func8;
import rx.functions.Func9;
import rx.functions.FuncN;
import rx.functions.Functions;
import rx.internal.operators.CachedObservable;
import rx.internal.operators.OnSubscribeAmb;
import rx.internal.operators.OnSubscribeCombineLatest;
import rx.internal.operators.OnSubscribeDefer;
import rx.internal.operators.OnSubscribeDelaySubscription;
import rx.internal.operators.OnSubscribeDelaySubscriptionWithSelector;
import rx.internal.operators.OnSubscribeFromCallable;
import rx.internal.operators.OnSubscribeFromIterable;
import rx.internal.operators.OnSubscribeGroupJoin;
import rx.internal.operators.OnSubscribeJoin;
import rx.internal.operators.OnSubscribeRange;
import rx.internal.operators.OnSubscribeRedo;
import rx.internal.operators.OnSubscribeSingle;
import rx.internal.operators.OnSubscribeTimerOnce;
import rx.internal.operators.OnSubscribeTimerPeriodically;
import rx.internal.operators.OnSubscribeToObservableFuture;
import rx.internal.operators.OnSubscribeUsing;
import rx.internal.operators.OperatorAll;
import rx.internal.operators.OperatorAny;
import rx.internal.operators.OperatorAsObservable;
import rx.internal.operators.OperatorBufferWithSingleObservable;
import rx.internal.operators.OperatorBufferWithSize;
import rx.internal.operators.OperatorBufferWithStartEndObservable;
import rx.internal.operators.OperatorBufferWithTime;
import rx.internal.operators.OperatorCast;
import rx.internal.operators.OperatorConcat;
import rx.internal.operators.OperatorDebounceWithSelector;
import rx.internal.operators.OperatorDebounceWithTime;
import rx.internal.operators.OperatorDelay;
import rx.internal.operators.OperatorDelayWithSelector;
import rx.internal.operators.OperatorDematerialize;
import rx.internal.operators.OperatorDistinct;
import rx.internal.operators.OperatorDistinctUntilChanged;
import rx.internal.operators.OperatorDoOnEach;
import rx.internal.operators.OperatorDoOnRequest;
import rx.internal.operators.OperatorDoOnSubscribe;
import rx.internal.operators.OperatorDoOnUnsubscribe;
import rx.internal.operators.OperatorEagerConcatMap;
import rx.internal.operators.OperatorElementAt;
import rx.internal.operators.OperatorFilter;
import rx.internal.operators.OperatorFinally;
import rx.internal.operators.OperatorGroupBy;
import rx.internal.operators.OperatorIgnoreElements;
import rx.internal.operators.OperatorMap;
import rx.internal.operators.OperatorMapNotification;
import rx.internal.operators.OperatorMapPair;
import rx.internal.operators.OperatorMaterialize;
import rx.internal.operators.OperatorMerge;
import rx.internal.operators.OperatorObserveOn;
import rx.internal.operators.OperatorOnBackpressureBuffer;
import rx.internal.operators.OperatorOnBackpressureDrop;
import rx.internal.operators.OperatorOnBackpressureLatest;
import rx.internal.operators.OperatorOnErrorResumeNextViaFunction;
import rx.internal.operators.OperatorOnErrorResumeNextViaObservable;
import rx.internal.operators.OperatorOnErrorReturn;
import rx.internal.operators.OperatorOnExceptionResumeNextViaObservable;
import rx.internal.operators.OperatorPublish;
import rx.internal.operators.OperatorReplay;
import rx.internal.operators.OperatorRetryWithPredicate;
import rx.internal.operators.OperatorSampleWithObservable;
import rx.internal.operators.OperatorSampleWithTime;
import rx.internal.operators.OperatorScan;
import rx.internal.operators.OperatorSequenceEqual;
import rx.internal.operators.OperatorSerialize;
import rx.internal.operators.OperatorSingle;
import rx.internal.operators.OperatorSkip;
import rx.internal.operators.OperatorSkipLast;
import rx.internal.operators.OperatorSkipLastTimed;
import rx.internal.operators.OperatorSkipTimed;
import rx.internal.operators.OperatorSkipUntil;
import rx.internal.operators.OperatorSkipWhile;
import rx.internal.operators.OperatorSubscribeOn;
import rx.internal.operators.OperatorSwitch;
import rx.internal.operators.OperatorSwitchIfEmpty;
import rx.internal.operators.OperatorTake;
import rx.internal.operators.OperatorTakeLast;
import rx.internal.operators.OperatorTakeLastOne;
import rx.internal.operators.OperatorTakeLastTimed;
import rx.internal.operators.OperatorTakeTimed;
import rx.internal.operators.OperatorTakeUntil;
import rx.internal.operators.OperatorTakeUntilPredicate;
import rx.internal.operators.OperatorTakeWhile;
import rx.internal.operators.OperatorThrottleFirst;
import rx.internal.operators.OperatorTimeInterval;
import rx.internal.operators.OperatorTimeout;
import rx.internal.operators.OperatorTimeoutWithSelector;
import rx.internal.operators.OperatorTimestamp;
import rx.internal.operators.OperatorToMap;
import rx.internal.operators.OperatorToMultimap;
import rx.internal.operators.OperatorToObservableList;
import rx.internal.operators.OperatorToObservableSortedList;
import rx.internal.operators.OperatorUnsubscribeOn;
import rx.internal.operators.OperatorWindowWithObservable;
import rx.internal.operators.OperatorWindowWithObservableFactory;
import rx.internal.operators.OperatorWindowWithSize;
import rx.internal.operators.OperatorWindowWithStartEndObservable;
import rx.internal.operators.OperatorWindowWithTime;
import rx.internal.operators.OperatorWithLatestFrom;
import rx.internal.operators.OperatorZip;
import rx.internal.operators.OperatorZipIterable;
import rx.internal.producers.SingleProducer;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.ScalarSynchronousObservable;
import rx.internal.util.UtilityFunctions;
import rx.observables.BlockingObservable;
import rx.observables.ConnectableObservable;
import rx.observables.GroupedObservable;
import rx.observers.SafeSubscriber;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public class Observable<T> {
    private static final RxJavaObservableExecutionHook hook = RxJavaPlugins.getInstance().getObservableExecutionHook();
    final OnSubscribe<T> onSubscribe;

    public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
    }

    public interface Operator<R, T> extends Func1<Subscriber<? super R>, Subscriber<? super T>> {
    }

    public interface Transformer<T, R> extends Func1<Observable<T>, Observable<R>> {
    }

    protected Observable(OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

    public static final <T> Observable<T> create(OnSubscribe<T> f) {
        return new Observable<>(hook.onCreate(f));
    }

    @Experimental
    public <R> R extend(Func1<? super OnSubscribe<T>, ? extends R> conversion) {
        return conversion.call(new OnSubscribe<T>() { // from class: rx.Observable.1
            @Override // rx.functions.Action1
            public void call(Subscriber<? super T> subscriber) {
                subscriber.add(Observable.subscribe(subscriber, Observable.this));
            }
        });
    }

    public final <R> Observable<R> lift(final Operator<? extends R, ? super T> operator) {
        return new Observable<>(new OnSubscribe<R>() { // from class: rx.Observable.2
            @Override // rx.functions.Action1
            public void call(Subscriber<? super R> subscriber) {
                try {
                    Subscriber subscriber2 = (Subscriber) Observable.hook.onLift(operator).call(subscriber);
                    try {
                        subscriber2.onStart();
                        Observable.this.onSubscribe.call(subscriber2);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        subscriber2.onError(th);
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    subscriber.onError(th2);
                }
            }
        });
    }

    public <R> Observable<R> compose(Transformer<? super T, ? extends R> transformer) {
        return (Observable) transformer.call(this);
    }

    @Beta
    public Single<T> toSingle() {
        return new Single<>(OnSubscribeSingle.create(this));
    }

    public static final <T> Observable<T> amb(Iterable<? extends Observable<? extends T>> sources) {
        return create(OnSubscribeAmb.amb(sources));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2) {
        return create(OnSubscribeAmb.amb(o1, o2));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3) {
        return create(OnSubscribeAmb.amb(o1, o2, o3));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7, o8));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8, Observable<? extends T> o9) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7, o8, o9));
    }

    public static final <T1, T2, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6, o7), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Observable<? extends T9> o9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8, o9), Functions.fromFunc(combineFunction));
    }

    public static final <T, R> Observable<R> combineLatest(List<? extends Observable<? extends T>> sources, FuncN<? extends R> combineFunction) {
        return create(new OnSubscribeCombineLatest(sources, combineFunction));
    }

    public static final <T> Observable<T> concat(Observable<? extends Observable<? extends T>> observable) {
        return (Observable<T>) observable.lift(OperatorConcat.instance());
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2) {
        return concat(just(t1, t2));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return concat(just(t1, t2, t3));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return concat(just(t1, t2, t3, t4));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return concat(just(t1, t2, t3, t4, t5));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return concat(just(t1, t2, t3, t4, t5, t6));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    public static final <T> Observable<T> defer(Func0<Observable<T>> observableFactory) {
        return create(new OnSubscribeDefer(observableFactory));
    }

    private static final class EmptyHolder {
        static final Observable<Object> INSTANCE = Observable.create(new OnSubscribe<Object>() { // from class: rx.Observable.EmptyHolder.1
            @Override // rx.functions.Action1
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onCompleted();
            }
        });

        private EmptyHolder() {
        }
    }

    public static final <T> Observable<T> empty() {
        return (Observable<T>) EmptyHolder.INSTANCE;
    }

    public static final <T> Observable<T> error(Throwable exception) {
        return new ThrowObservable(exception);
    }

    public static final <T> Observable<T> from(Future<? extends T> future) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future));
    }

    public static final <T> Observable<T> from(Future<? extends T> future, long timeout, TimeUnit unit) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future, timeout, unit));
    }

    public static final <T> Observable<T> from(Future<? extends T> future, Scheduler scheduler) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future)).subscribeOn(scheduler);
    }

    public static final <T> Observable<T> from(Iterable<? extends T> iterable) {
        return create(new OnSubscribeFromIterable(iterable));
    }

    public static final <T> Observable<T> from(T[] array) {
        return from(Arrays.asList(array));
    }

    @Experimental
    public static <T> Observable<T> fromCallable(Callable<? extends T> func) {
        return create(new OnSubscribeFromCallable(func));
    }

    public static final Observable<Long> interval(long interval, TimeUnit unit) {
        return interval(interval, interval, unit, Schedulers.computation());
    }

    public static final Observable<Long> interval(long interval, TimeUnit unit, Scheduler scheduler) {
        return interval(interval, interval, unit, scheduler);
    }

    public static final Observable<Long> interval(long initialDelay, long period, TimeUnit unit) {
        return interval(initialDelay, period, unit, Schedulers.computation());
    }

    public static final Observable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeTimerPeriodically(initialDelay, period, unit, scheduler));
    }

    public static final <T> Observable<T> just(T value) {
        return ScalarSynchronousObservable.create(value);
    }

    public static final <T> Observable<T> just(T t1, T t2) {
        return from(Arrays.asList(t1, t2));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3) {
        return from(Arrays.asList(t1, t2, t3));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4) {
        return from(Arrays.asList(t1, t2, t3, t4));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5) {
        return from(Arrays.asList(t1, t2, t3, t4, t5));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9, T t10) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    public static final <T> Observable<T> merge(Iterable<? extends Observable<? extends T>> sequences) {
        return merge(from(sequences));
    }

    public static final <T> Observable<T> merge(Iterable<? extends Observable<? extends T>> sequences, int maxConcurrent) {
        return merge(from(sequences), maxConcurrent);
    }

    public static final <T> Observable<T> merge(Observable<? extends Observable<? extends T>> observable) {
        return observable.getClass() == ScalarSynchronousObservable.class ? ((ScalarSynchronousObservable) observable).scalarFlatMap(UtilityFunctions.identity()) : (Observable<T>) observable.lift(OperatorMerge.instance(false));
    }

    public static final <T> Observable<T> merge(Observable<? extends Observable<? extends T>> observable, int i) {
        return observable.getClass() == ScalarSynchronousObservable.class ? ((ScalarSynchronousObservable) observable).scalarFlatMap(UtilityFunctions.identity()) : (Observable<T>) observable.lift(OperatorMerge.instance(false, i));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2) {
        return merge(from(Arrays.asList(t1, t2)));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return merge(from(Arrays.asList(t1, t2, t3)));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return merge(from(Arrays.asList(t1, t2, t3, t4)));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5)));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5, t6)));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7)));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8)));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9)));
    }

    public static final <T> Observable<T> merge(Observable<? extends T>[] sequences) {
        return merge(from(sequences));
    }

    public static final <T> Observable<T> merge(Observable<? extends T>[] sequences, int maxConcurrent) {
        return merge(from(sequences), maxConcurrent);
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends Observable<? extends T>> observable) {
        return (Observable<T>) observable.lift(OperatorMerge.instance(true));
    }

    @Experimental
    public static final <T> Observable<T> mergeDelayError(Observable<? extends Observable<? extends T>> observable, int i) {
        return (Observable<T>) observable.lift(OperatorMerge.instance(true, i));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2) {
        return mergeDelayError(just(t1, t2));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return mergeDelayError(just(t1, t2, t3));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return mergeDelayError(just(t1, t2, t3, t4));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return mergeDelayError(just(t1, t2, t3, t4, t5));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6, t7));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    public final Observable<Observable<T>> nest() {
        return just(this);
    }

    public static final <T> Observable<T> never() {
        return NeverObservable.instance();
    }

    public static final Observable<Integer> range(int start, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count can not be negative");
        }
        if (count == 0) {
            return empty();
        }
        if (start > (PriorityMap.PRIORITY_MAX - count) + 1) {
            throw new IllegalArgumentException("start + count can not exceed Integer.MAX_VALUE");
        }
        if (count == 1) {
            return just(Integer.valueOf(start));
        }
        return create(new OnSubscribeRange(start, (count - 1) + start));
    }

    public static final Observable<Integer> range(int start, int count, Scheduler scheduler) {
        return range(start, count).subscribeOn(scheduler);
    }

    public static final <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second) {
        return sequenceEqual(first, second, new Func2<T, T, Boolean>() { // from class: rx.Observable.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // rx.functions.Func2
            public final Boolean call(T first2, T second2) {
                if (first2 == null) {
                    return Boolean.valueOf(second2 == null);
                }
                return Boolean.valueOf(first2.equals(second2));
            }
        });
    }

    public static final <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second, Func2<? super T, ? super T, Boolean> equality) {
        return OperatorSequenceEqual.sequenceEqual(first, second, equality);
    }

    public static final <T> Observable<T> switchOnNext(Observable<? extends Observable<? extends T>> observable) {
        return (Observable<T>) observable.lift(OperatorSwitch.instance());
    }

    @Deprecated
    public static final Observable<Long> timer(long initialDelay, long period, TimeUnit unit) {
        return interval(initialDelay, period, unit, Schedulers.computation());
    }

    @Deprecated
    public static final Observable<Long> timer(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return interval(initialDelay, period, unit, scheduler);
    }

    public static final Observable<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    public static final Observable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeTimerOnce(delay, unit, scheduler));
    }

    public static final <T, Resource> Observable<T> using(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Observable<? extends T>> observableFactory, Action1<? super Resource> disposeAction) {
        return using(resourceFactory, observableFactory, disposeAction, false);
    }

    @Experimental
    public static final <T, Resource> Observable<T> using(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Observable<? extends T>> observableFactory, Action1<? super Resource> disposeAction, boolean disposeEagerly) {
        return create(new OnSubscribeUsing(resourceFactory, observableFactory, disposeAction, disposeEagerly));
    }

    public static final <R> Observable<R> zip(Iterable<? extends Observable<?>> ws, FuncN<? extends R> zipFunction) {
        List<Observable<?>> os = new ArrayList<>();
        for (Observable<?> o : ws) {
            os.add(o);
        }
        return just(os.toArray(new Observable[os.size()])).lift(new OperatorZip(zipFunction));
    }

    public static final <R> Observable<R> zip(Observable<? extends Observable<?>> ws, FuncN<? extends R> zipFunction) {
        return ws.toList().map(new Func1<List<? extends Observable<?>>, Observable<?>[]>() { // from class: rx.Observable.4
            @Override // rx.functions.Func1
            public Observable<?>[] call(List<? extends Observable<?>> o) {
                return (Observable[]) o.toArray(new Observable[o.size()]);
            }
        }).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6, o7}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Observable<? extends T9> o9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8, o9}).lift(new OperatorZip(zipFunction));
    }

    public final Observable<Boolean> all(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorAll(predicate));
    }

    public final Observable<T> ambWith(Observable<? extends T> t1) {
        return amb(this, t1);
    }

    public final Observable<T> asObservable() {
        return (Observable<T>) lift(OperatorAsObservable.instance());
    }

    public final <TClosing> Observable<List<T>> buffer(Func0<? extends Observable<? extends TClosing>> func0) {
        return (Observable<List<T>>) lift(new OperatorBufferWithSingleObservable(func0, 16));
    }

    public final Observable<List<T>> buffer(int count) {
        return buffer(count, count);
    }

    public final Observable<List<T>> buffer(int i, int i2) {
        return (Observable<List<T>>) lift(new OperatorBufferWithSize(i, i2));
    }

    public final Observable<List<T>> buffer(long timespan, long timeshift, TimeUnit unit) {
        return buffer(timespan, timeshift, unit, Schedulers.computation());
    }

    public final Observable<List<T>> buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<List<T>>) lift(new OperatorBufferWithTime(j, j2, timeUnit, PriorityMap.PRIORITY_MAX, scheduler));
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit) {
        return buffer(timespan, unit, PriorityMap.PRIORITY_MAX, Schedulers.computation());
    }

    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit, int i) {
        return (Observable<List<T>>) lift(new OperatorBufferWithTime(j, j, timeUnit, i, Schedulers.computation()));
    }

    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit, int i, Scheduler scheduler) {
        return (Observable<List<T>>) lift(new OperatorBufferWithTime(j, j, timeUnit, i, scheduler));
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler) {
        return buffer(timespan, timespan, unit, scheduler);
    }

    public final <TOpening, TClosing> Observable<List<T>> buffer(Observable<? extends TOpening> observable, Func1<? super TOpening, ? extends Observable<? extends TClosing>> func1) {
        return (Observable<List<T>>) lift(new OperatorBufferWithStartEndObservable(observable, func1));
    }

    public final <B> Observable<List<T>> buffer(Observable<B> boundary) {
        return buffer(boundary, 16);
    }

    public final <B> Observable<List<T>> buffer(Observable<B> observable, int i) {
        return (Observable<List<T>>) lift(new OperatorBufferWithSingleObservable(observable, i));
    }

    public final Observable<T> cache() {
        return CachedObservable.from(this);
    }

    public final Observable<T> cache(int capacityHint) {
        return CachedObservable.from(this, capacityHint);
    }

    public final <R> Observable<R> cast(Class<R> klass) {
        return lift(new OperatorCast(klass));
    }

    public final <R> Observable<R> collect(Func0<R> stateFactory, final Action2<R, ? super T> collector) {
        Func2<R, T, R> accumulator = new Func2<R, T, R>() { // from class: rx.Observable.5
            @Override // rx.functions.Func2
            public final R call(R state, T value) {
                collector.call(state, value);
                return state;
            }
        };
        return lift(new OperatorScan((Func0) stateFactory, (Func2) accumulator)).last();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <R> Observable<R> concatMap(Func1<? super T, ? extends Observable<? extends R>> func1) {
        return concat(map(func1));
    }

    public final Observable<T> concatWith(Observable<? extends T> t1) {
        return concat(this, t1);
    }

    public final Observable<Boolean> contains(final Object element) {
        return exists(new Func1<T, Boolean>() { // from class: rx.Observable.6
            @Override // rx.functions.Func1
            public /* bridge */ /* synthetic */ Boolean call(Object x0) {
                return call((AnonymousClass6) x0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // rx.functions.Func1
            public final Boolean call(T t1) {
                return Boolean.valueOf(element == null ? t1 == null : element.equals(t1));
            }
        });
    }

    public final Observable<Integer> count() {
        return reduce(0, CountHolder.INSTANCE);
    }

    private static final class CountHolder {
        static final Func2<Integer, Object, Integer> INSTANCE = new Func2<Integer, Object, Integer>() { // from class: rx.Observable.CountHolder.1
            @Override // rx.functions.Func2
            public final Integer call(Integer count, Object o) {
                return Integer.valueOf(count.intValue() + 1);
            }
        };

        private CountHolder() {
        }
    }

    public final Observable<Long> countLong() {
        return reduce(0L, CountLongHolder.INSTANCE);
    }

    private static final class CountLongHolder {
        static final Func2<Long, Object, Long> INSTANCE = new Func2<Long, Object, Long>() { // from class: rx.Observable.CountLongHolder.1
            @Override // rx.functions.Func2
            public final Long call(Long count, Object o) {
                return Long.valueOf(count.longValue() + 1);
            }
        };

        private CountLongHolder() {
        }
    }

    public final <U> Observable<T> debounce(Func1<? super T, ? extends Observable<U>> func1) {
        return (Observable<T>) lift(new OperatorDebounceWithSelector(func1));
    }

    public final Observable<T> debounce(long timeout, TimeUnit unit) {
        return debounce(timeout, unit, Schedulers.computation());
    }

    public final Observable<T> debounce(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorDebounceWithTime(j, timeUnit, scheduler));
    }

    public final Observable<T> defaultIfEmpty(final T defaultValue) {
        return switchIfEmpty(create(new OnSubscribe<T>() { // from class: rx.Observable.7
            @Override // rx.functions.Action1
            public void call(Subscriber<? super T> subscriber) {
                subscriber.setProducer(new SingleProducer(subscriber, defaultValue));
            }
        }));
    }

    public final Observable<T> switchIfEmpty(Observable<? extends T> observable) {
        return (Observable<T>) lift(new OperatorSwitchIfEmpty(observable));
    }

    public final <U, V> Observable<T> delay(Func0<? extends Observable<U>> func0, Func1<? super T, ? extends Observable<V>> func1) {
        return (Observable<T>) delaySubscription(func0).lift(new OperatorDelayWithSelector(this, func1));
    }

    public final <U> Observable<T> delay(Func1<? super T, ? extends Observable<U>> func1) {
        return (Observable<T>) lift(new OperatorDelayWithSelector(this, func1));
    }

    public final Observable<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation());
    }

    public final Observable<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorDelay(j, timeUnit, scheduler));
    }

    public final Observable<T> delaySubscription(long delay, TimeUnit unit) {
        return delaySubscription(delay, unit, Schedulers.computation());
    }

    public final Observable<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeDelaySubscription(this, delay, unit, scheduler));
    }

    public final <U> Observable<T> delaySubscription(Func0<? extends Observable<U>> subscriptionDelay) {
        return create(new OnSubscribeDelaySubscriptionWithSelector(this, subscriptionDelay));
    }

    public final <T2> Observable<T2> dematerialize() {
        return (Observable<T2>) lift(OperatorDematerialize.instance());
    }

    public final Observable<T> distinct() {
        return (Observable<T>) lift(OperatorDistinct.instance());
    }

    public final <U> Observable<T> distinct(Func1<? super T, ? extends U> func1) {
        return (Observable<T>) lift(new OperatorDistinct(func1));
    }

    public final Observable<T> distinctUntilChanged() {
        return (Observable<T>) lift(OperatorDistinctUntilChanged.instance());
    }

    public final <U> Observable<T> distinctUntilChanged(Func1<? super T, ? extends U> func1) {
        return (Observable<T>) lift(new OperatorDistinctUntilChanged(func1));
    }

    public final Observable<T> doOnCompleted(final Action0 action0) {
        return (Observable<T>) lift(new OperatorDoOnEach(new Observer<T>() { // from class: rx.Observable.8
            @Override // rx.Observer
            public final void onCompleted() {
                action0.call();
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
            }

            @Override // rx.Observer
            public final void onNext(T args) {
            }
        }));
    }

    public final Observable<T> doOnEach(final Action1<Notification<? super T>> action1) {
        return (Observable<T>) lift(new OperatorDoOnEach(new Observer<T>() { // from class: rx.Observable.9
            @Override // rx.Observer
            public final void onCompleted() {
                action1.call(Notification.createOnCompleted());
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                action1.call(Notification.createOnError(e));
            }

            @Override // rx.Observer
            public final void onNext(T v) {
                action1.call(Notification.createOnNext(v));
            }
        }));
    }

    public final Observable<T> doOnEach(Observer<? super T> observer) {
        return (Observable<T>) lift(new OperatorDoOnEach(observer));
    }

    public final Observable<T> doOnError(final Action1<Throwable> action1) {
        return (Observable<T>) lift(new OperatorDoOnEach(new Observer<T>() { // from class: rx.Observable.10
            @Override // rx.Observer
            public final void onCompleted() {
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                action1.call(e);
            }

            @Override // rx.Observer
            public final void onNext(T args) {
            }
        }));
    }

    public final Observable<T> doOnNext(final Action1<? super T> action1) {
        return (Observable<T>) lift(new OperatorDoOnEach(new Observer<T>() { // from class: rx.Observable.11
            @Override // rx.Observer
            public final void onCompleted() {
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
            }

            @Override // rx.Observer
            public final void onNext(T args) {
                action1.call(args);
            }
        }));
    }

    @Beta
    public final Observable<T> doOnRequest(Action1<Long> action1) {
        return (Observable<T>) lift(new OperatorDoOnRequest(action1));
    }

    public final Observable<T> doOnSubscribe(Action0 action0) {
        return (Observable<T>) lift(new OperatorDoOnSubscribe(action0));
    }

    public final Observable<T> doOnTerminate(final Action0 action0) {
        return (Observable<T>) lift(new OperatorDoOnEach(new Observer<T>() { // from class: rx.Observable.12
            @Override // rx.Observer
            public final void onCompleted() {
                action0.call();
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                action0.call();
            }

            @Override // rx.Observer
            public final void onNext(T args) {
            }
        }));
    }

    public final Observable<T> doOnUnsubscribe(Action0 action0) {
        return (Observable<T>) lift(new OperatorDoOnUnsubscribe(action0));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2) {
        return concatEager(Arrays.asList(o1, o2));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3) {
        return concatEager(Arrays.asList(o1, o2, o3));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4) {
        return concatEager(Arrays.asList(o1, o2, o3, o4));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5, o6));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5, o6, o7));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8, Observable<? extends T> o9) {
        return concatEager(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8, o9));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Iterable<? extends Observable<? extends T>> sources) {
        return from(sources).concatMapEager(UtilityFunctions.identity());
    }

    @Experimental
    public static <T> Observable<T> concatEager(Iterable<? extends Observable<? extends T>> sources, int capacityHint) {
        return from(sources).concatMapEager(UtilityFunctions.identity(), capacityHint);
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends Observable<? extends T>> observable) {
        return (Observable<T>) observable.concatMapEager(UtilityFunctions.identity());
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends Observable<? extends T>> observable, int i) {
        return (Observable<T>) observable.concatMapEager(UtilityFunctions.identity(), i);
    }

    @Experimental
    public final <R> Observable<R> concatMapEager(Func1<? super T, ? extends Observable<? extends R>> mapper) {
        return concatMapEager(mapper, RxRingBuffer.SIZE);
    }

    @Experimental
    public final <R> Observable<R> concatMapEager(Func1<? super T, ? extends Observable<? extends R>> mapper, int capacityHint) {
        if (capacityHint < 1) {
            throw new IllegalArgumentException("capacityHint > 0 required but it was " + capacityHint);
        }
        return lift(new OperatorEagerConcatMap(mapper, capacityHint));
    }

    public final Observable<T> elementAt(int i) {
        return (Observable<T>) lift(new OperatorElementAt(i));
    }

    public final Observable<T> elementAtOrDefault(int i, T t) {
        return (Observable<T>) lift(new OperatorElementAt(i, t));
    }

    public final Observable<Boolean> exists(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorAny(predicate, false));
    }

    public final Observable<T> filter(Func1<? super T, Boolean> func1) {
        return (Observable<T>) lift(new OperatorFilter(func1));
    }

    public final Observable<T> finallyDo(Action0 action0) {
        return (Observable<T>) lift(new OperatorFinally(action0));
    }

    public final Observable<T> first() {
        return take(1).single();
    }

    public final Observable<T> first(Func1<? super T, Boolean> predicate) {
        return takeFirst(predicate).single();
    }

    public final Observable<T> firstOrDefault(T defaultValue) {
        return take(1).singleOrDefault(defaultValue);
    }

    public final Observable<T> firstOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return takeFirst(predicate).singleOrDefault(defaultValue);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func1) {
        return getClass() == ScalarSynchronousObservable.class ? ((ScalarSynchronousObservable) this).scalarFlatMap(func1) : merge(map(func1));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Beta
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func1, int maxConcurrent) {
        return getClass() == ScalarSynchronousObservable.class ? ((ScalarSynchronousObservable) this).scalarFlatMap(func1) : merge(map(func1), maxConcurrent);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func1, Func1<? super Throwable, ? extends Observable<? extends R>> func12, Func0<? extends Observable<? extends R>> func0) {
        return merge(mapNotification(func1, func12, func0));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Beta
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func1, Func1<? super Throwable, ? extends Observable<? extends R>> func12, Func0<? extends Observable<? extends R>> func0, int maxConcurrent) {
        return merge(mapNotification(func1, func12, func0), maxConcurrent);
    }

    public final <U, R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector) {
        return merge(lift(new OperatorMapPair(collectionSelector, resultSelector)));
    }

    @Beta
    public final <U, R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector, int maxConcurrent) {
        return merge(lift(new OperatorMapPair(collectionSelector, resultSelector)), maxConcurrent);
    }

    public final <R> Observable<R> flatMapIterable(Func1<? super T, ? extends Iterable<? extends R>> collectionSelector) {
        return merge(map(OperatorMapPair.convertSelector(collectionSelector)));
    }

    public final <U, R> Observable<R> flatMapIterable(Func1<? super T, ? extends Iterable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector) {
        return flatMap(OperatorMapPair.convertSelector(collectionSelector), resultSelector);
    }

    public final void forEach(Action1<? super T> onNext) {
        subscribe(onNext);
    }

    public final void forEach(Action1<? super T> onNext, Action1<Throwable> onError) {
        subscribe(onNext, onError);
    }

    public final void forEach(Action1<? super T> onNext, Action1<Throwable> onError, Action0 onComplete) {
        subscribe(onNext, onError, onComplete);
    }

    public final <K, R> Observable<GroupedObservable<K, R>> groupBy(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends R> func12) {
        return lift(new OperatorGroupBy(func1, func12));
    }

    public final <K> Observable<GroupedObservable<K, T>> groupBy(Func1<? super T, ? extends K> func1) {
        return (Observable<GroupedObservable<K, T>>) lift(new OperatorGroupBy(func1));
    }

    public final <T2, D1, D2, R> Observable<R> groupJoin(Observable<T2> right, Func1<? super T, ? extends Observable<D1>> leftDuration, Func1<? super T2, ? extends Observable<D2>> rightDuration, Func2<? super T, ? super Observable<T2>, ? extends R> resultSelector) {
        return create(new OnSubscribeGroupJoin(this, right, leftDuration, rightDuration, resultSelector));
    }

    public final Observable<T> ignoreElements() {
        return (Observable<T>) lift(OperatorIgnoreElements.instance());
    }

    public final Observable<Boolean> isEmpty() {
        return lift(HolderAnyForEmpty.INSTANCE);
    }

    private static class HolderAnyForEmpty {
        static final OperatorAny<?> INSTANCE = new OperatorAny<>(UtilityFunctions.alwaysTrue(), true);

        private HolderAnyForEmpty() {
        }
    }

    public final <TRight, TLeftDuration, TRightDuration, R> Observable<R> join(Observable<TRight> right, Func1<T, Observable<TLeftDuration>> leftDurationSelector, Func1<TRight, Observable<TRightDuration>> rightDurationSelector, Func2<T, TRight, R> resultSelector) {
        return create(new OnSubscribeJoin(this, right, leftDurationSelector, rightDurationSelector, resultSelector));
    }

    public final Observable<T> last() {
        return takeLast(1).single();
    }

    public final Observable<T> last(Func1<? super T, Boolean> predicate) {
        return filter(predicate).takeLast(1).single();
    }

    public final Observable<T> lastOrDefault(T defaultValue) {
        return takeLast(1).singleOrDefault(defaultValue);
    }

    public final Observable<T> lastOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return filter(predicate).takeLast(1).singleOrDefault(defaultValue);
    }

    public final Observable<T> limit(int count) {
        return take(count);
    }

    public final <R> Observable<R> map(Func1<? super T, ? extends R> func) {
        return lift(new OperatorMap(func));
    }

    private final <R> Observable<R> mapNotification(Func1<? super T, ? extends R> onNext, Func1<? super Throwable, ? extends R> onError, Func0<? extends R> onCompleted) {
        return lift(new OperatorMapNotification(onNext, onError, onCompleted));
    }

    public final Observable<Notification<T>> materialize() {
        return (Observable<Notification<T>>) lift(OperatorMaterialize.instance());
    }

    public final Observable<T> mergeWith(Observable<? extends T> t1) {
        return merge(this, t1);
    }

    public final Observable<T> observeOn(Scheduler scheduler) {
        return this instanceof ScalarSynchronousObservable ? ((ScalarSynchronousObservable) this).scalarScheduleOn(scheduler) : (Observable<T>) lift(new OperatorObserveOn(scheduler));
    }

    public final <R> Observable<R> ofType(final Class<R> klass) {
        return filter(new Func1<T, Boolean>() { // from class: rx.Observable.13
            @Override // rx.functions.Func1
            public /* bridge */ /* synthetic */ Boolean call(Object x0) {
                return call((AnonymousClass13) x0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // rx.functions.Func1
            public final Boolean call(T t) {
                return Boolean.valueOf(klass.isInstance(t));
            }
        }).cast(klass);
    }

    public final Observable<T> onBackpressureBuffer() {
        return (Observable<T>) lift(OperatorOnBackpressureBuffer.instance());
    }

    public final Observable<T> onBackpressureBuffer(long j) {
        return (Observable<T>) lift(new OperatorOnBackpressureBuffer(j));
    }

    public final Observable<T> onBackpressureBuffer(long j, Action0 action0) {
        return (Observable<T>) lift(new OperatorOnBackpressureBuffer(j, action0));
    }

    public final Observable<T> onBackpressureDrop(Action1<? super T> action1) {
        return (Observable<T>) lift(new OperatorOnBackpressureDrop(action1));
    }

    public final Observable<T> onBackpressureDrop() {
        return (Observable<T>) lift(OperatorOnBackpressureDrop.instance());
    }

    public final Observable<T> onBackpressureLatest() {
        return (Observable<T>) lift(OperatorOnBackpressureLatest.instance());
    }

    public final Observable<T> onErrorResumeNext(Func1<Throwable, ? extends Observable<? extends T>> func1) {
        return (Observable<T>) lift(new OperatorOnErrorResumeNextViaFunction(func1));
    }

    public final Observable<T> onErrorResumeNext(Observable<? extends T> observable) {
        return (Observable<T>) lift(new OperatorOnErrorResumeNextViaObservable(observable));
    }

    public final Observable<T> onErrorReturn(Func1<Throwable, ? extends T> func1) {
        return (Observable<T>) lift(new OperatorOnErrorReturn(func1));
    }

    public final Observable<T> onExceptionResumeNext(Observable<? extends T> observable) {
        return (Observable<T>) lift(new OperatorOnExceptionResumeNextViaObservable(observable));
    }

    public final ConnectableObservable<T> publish() {
        return OperatorPublish.create(this);
    }

    public final <R> Observable<R> publish(Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return OperatorPublish.create(this, selector);
    }

    public final Observable<T> reduce(Func2<T, T, T> accumulator) {
        return scan(accumulator).last();
    }

    public final <R> Observable<R> reduce(R initialValue, Func2<R, ? super T, R> accumulator) {
        return scan(initialValue, accumulator).takeLast(1);
    }

    public final Observable<T> repeat() {
        return OnSubscribeRedo.repeat(this);
    }

    public final Observable<T> repeat(Scheduler scheduler) {
        return OnSubscribeRedo.repeat(this, scheduler);
    }

    public final Observable<T> repeat(long count) {
        return OnSubscribeRedo.repeat(this, count);
    }

    public final Observable<T> repeat(long count, Scheduler scheduler) {
        return OnSubscribeRedo.repeat(this, count, scheduler);
    }

    public final Observable<T> repeatWhen(final Func1<? super Observable<? extends Void>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> dematerializedNotificationHandler = new Func1<Observable<? extends Notification<?>>, Observable<?>>() { // from class: rx.Observable.14
            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.functions.Func1
            public Observable<?> call(Observable<? extends Notification<?>> notifications) {
                return (Observable) notificationHandler.call(notifications.map(new Func1<Notification<?>, Void>() { // from class: rx.Observable.14.1
                    @Override // rx.functions.Func1
                    public Void call(Notification<?> notification) {
                        return null;
                    }
                }));
            }
        };
        return OnSubscribeRedo.repeat(this, dematerializedNotificationHandler, scheduler);
    }

    public final Observable<T> repeatWhen(final Func1<? super Observable<? extends Void>, ? extends Observable<?>> notificationHandler) {
        Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> dematerializedNotificationHandler = new Func1<Observable<? extends Notification<?>>, Observable<?>>() { // from class: rx.Observable.15
            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.functions.Func1
            public Observable<?> call(Observable<? extends Notification<?>> notifications) {
                return (Observable) notificationHandler.call(notifications.map(new Func1<Notification<?>, Void>() { // from class: rx.Observable.15.1
                    @Override // rx.functions.Func1
                    public Void call(Notification<?> notification) {
                        return null;
                    }
                }));
            }
        };
        return OnSubscribeRedo.repeat(this, dematerializedNotificationHandler);
    }

    public final ConnectableObservable<T> replay() {
        return OperatorReplay.create(this);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() { // from class: rx.Observable.16
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public ConnectableObservable<T> call() {
                return Observable.this.replay();
            }
        }, selector);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize) {
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() { // from class: rx.Observable.17
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public ConnectableObservable<T> call() {
                return Observable.this.replay(bufferSize);
            }
        }, selector);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, int bufferSize, long time, TimeUnit unit) {
        return replay(selector, bufferSize, time, unit, Schedulers.computation());
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize, final long time, final TimeUnit unit, final Scheduler scheduler) {
        if (bufferSize < 0) {
            throw new IllegalArgumentException("bufferSize < 0");
        }
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() { // from class: rx.Observable.18
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public ConnectableObservable<T> call() {
                return Observable.this.replay(bufferSize, time, unit, scheduler);
            }
        }, selector);
    }

    public final <R> Observable<R> replay(final Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize, final Scheduler scheduler) {
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() { // from class: rx.Observable.19
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public ConnectableObservable<T> call() {
                return Observable.this.replay(bufferSize);
            }
        }, new Func1<Observable<T>, Observable<R>>() { // from class: rx.Observable.20
            @Override // rx.functions.Func1
            public Observable<R> call(Observable<T> t) {
                return ((Observable) selector.call(t)).observeOn(scheduler);
            }
        });
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, long time, TimeUnit unit) {
        return replay(selector, time, unit, Schedulers.computation());
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, final long time, final TimeUnit unit, final Scheduler scheduler) {
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() { // from class: rx.Observable.21
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public ConnectableObservable<T> call() {
                return Observable.this.replay(time, unit, scheduler);
            }
        }, selector);
    }

    public final <R> Observable<R> replay(final Func1<? super Observable<T>, ? extends Observable<R>> selector, final Scheduler scheduler) {
        return OperatorReplay.multicastSelector(new Func0<ConnectableObservable<T>>() { // from class: rx.Observable.22
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public ConnectableObservable<T> call() {
                return Observable.this.replay();
            }
        }, new Func1<Observable<T>, Observable<R>>() { // from class: rx.Observable.23
            @Override // rx.functions.Func1
            public Observable<R> call(Observable<T> t) {
                return ((Observable) selector.call(t)).observeOn(scheduler);
            }
        });
    }

    public final ConnectableObservable<T> replay(int bufferSize) {
        return OperatorReplay.create(this, bufferSize);
    }

    public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit) {
        return replay(bufferSize, time, unit, Schedulers.computation());
    }

    public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        if (bufferSize < 0) {
            throw new IllegalArgumentException("bufferSize < 0");
        }
        return OperatorReplay.create(this, time, unit, scheduler, bufferSize);
    }

    public final ConnectableObservable<T> replay(int bufferSize, Scheduler scheduler) {
        return OperatorReplay.observeOn(replay(bufferSize), scheduler);
    }

    public final ConnectableObservable<T> replay(long time, TimeUnit unit) {
        return replay(time, unit, Schedulers.computation());
    }

    public final ConnectableObservable<T> replay(long time, TimeUnit unit, Scheduler scheduler) {
        return OperatorReplay.create(this, time, unit, scheduler);
    }

    public final ConnectableObservable<T> replay(Scheduler scheduler) {
        return OperatorReplay.observeOn(replay(), scheduler);
    }

    public final Observable<T> retry() {
        return OnSubscribeRedo.retry(this);
    }

    public final Observable<T> retry(long count) {
        return OnSubscribeRedo.retry(this, count);
    }

    public final Observable<T> retry(Func2<Integer, Throwable, Boolean> func2) {
        return (Observable<T>) nest().lift(new OperatorRetryWithPredicate(func2));
    }

    public final Observable<T> retryWhen(final Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler) {
        Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> dematerializedNotificationHandler = new Func1<Observable<? extends Notification<?>>, Observable<?>>() { // from class: rx.Observable.24
            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.functions.Func1
            public Observable<?> call(Observable<? extends Notification<?>> notifications) {
                return (Observable) notificationHandler.call(notifications.map(new Func1<Notification<?>, Throwable>() { // from class: rx.Observable.24.1
                    @Override // rx.functions.Func1
                    public Throwable call(Notification<?> notification) {
                        return notification.getThrowable();
                    }
                }));
            }
        };
        return OnSubscribeRedo.retry(this, dematerializedNotificationHandler);
    }

    public final Observable<T> retryWhen(final Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> dematerializedNotificationHandler = new Func1<Observable<? extends Notification<?>>, Observable<?>>() { // from class: rx.Observable.25
            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.functions.Func1
            public Observable<?> call(Observable<? extends Notification<?>> notifications) {
                return (Observable) notificationHandler.call(notifications.map(new Func1<Notification<?>, Throwable>() { // from class: rx.Observable.25.1
                    @Override // rx.functions.Func1
                    public Throwable call(Notification<?> notification) {
                        return notification.getThrowable();
                    }
                }));
            }
        };
        return OnSubscribeRedo.retry(this, dematerializedNotificationHandler, scheduler);
    }

    public final Observable<T> sample(long period, TimeUnit unit) {
        return sample(period, unit, Schedulers.computation());
    }

    public final Observable<T> sample(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorSampleWithTime(j, timeUnit, scheduler));
    }

    public final <U> Observable<T> sample(Observable<U> observable) {
        return (Observable<T>) lift(new OperatorSampleWithObservable(observable));
    }

    public final Observable<T> scan(Func2<T, T, T> func2) {
        return (Observable<T>) lift(new OperatorScan(func2));
    }

    public final <R> Observable<R> scan(R initialValue, Func2<R, ? super T, R> accumulator) {
        return lift(new OperatorScan(initialValue, accumulator));
    }

    public final Observable<T> serialize() {
        return (Observable<T>) lift(OperatorSerialize.instance());
    }

    public final Observable<T> share() {
        return publish().refCount();
    }

    public final Observable<T> single() {
        return (Observable<T>) lift(OperatorSingle.instance());
    }

    public final Observable<T> single(Func1<? super T, Boolean> predicate) {
        return filter(predicate).single();
    }

    public final Observable<T> singleOrDefault(T t) {
        return (Observable<T>) lift(new OperatorSingle(t));
    }

    public final Observable<T> singleOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return filter(predicate).singleOrDefault(defaultValue);
    }

    public final Observable<T> skip(int i) {
        return (Observable<T>) lift(new OperatorSkip(i));
    }

    public final Observable<T> skip(long time, TimeUnit unit) {
        return skip(time, unit, Schedulers.computation());
    }

    public final Observable<T> skip(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorSkipTimed(j, timeUnit, scheduler));
    }

    public final Observable<T> skipLast(int i) {
        return (Observable<T>) lift(new OperatorSkipLast(i));
    }

    public final Observable<T> skipLast(long time, TimeUnit unit) {
        return skipLast(time, unit, Schedulers.computation());
    }

    public final Observable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorSkipLastTimed(j, timeUnit, scheduler));
    }

    public final <U> Observable<T> skipUntil(Observable<U> observable) {
        return (Observable<T>) lift(new OperatorSkipUntil(observable));
    }

    public final Observable<T> skipWhile(Func1<? super T, Boolean> func1) {
        return (Observable<T>) lift(new OperatorSkipWhile(OperatorSkipWhile.toPredicate2(func1)));
    }

    public final Observable<T> startWith(Observable<T> values) {
        return concat(values, this);
    }

    public final Observable<T> startWith(Iterable<T> values) {
        return concat(from(values), this);
    }

    public final Observable<T> startWith(T t1) {
        return concat(just(t1), this);
    }

    public final Observable<T> startWith(T t1, T t2) {
        return concat(just(t1, t2), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3) {
        return concat(just(t1, t2, t3), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4) {
        return concat(just(t1, t2, t3, t4), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5) {
        return concat(just(t1, t2, t3, t4, t5), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6) {
        return concat(just(t1, t2, t3, t4, t5, t6), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8, t9), this);
    }

    public final Subscription subscribe() {
        return subscribe((Subscriber) new Subscriber<T>() { // from class: rx.Observable.26
            @Override // rx.Observer
            public final void onCompleted() {
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            @Override // rx.Observer
            public final void onNext(T args) {
            }
        });
    }

    public final Subscription subscribe(final Action1<? super T> onNext) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        return subscribe((Subscriber) new Subscriber<T>() { // from class: rx.Observable.27
            @Override // rx.Observer
            public final void onCompleted() {
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            @Override // rx.Observer
            public final void onNext(T args) {
                onNext.call(args);
            }
        });
    }

    public final Subscription subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        }
        return subscribe((Subscriber) new Subscriber<T>() { // from class: rx.Observable.28
            @Override // rx.Observer
            public final void onCompleted() {
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                onError.call(e);
            }

            @Override // rx.Observer
            public final void onNext(T args) {
                onNext.call(args);
            }
        });
    }

    public final Subscription subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        }
        if (onComplete == null) {
            throw new IllegalArgumentException("onComplete can not be null");
        }
        return subscribe((Subscriber) new Subscriber<T>() { // from class: rx.Observable.29
            @Override // rx.Observer
            public final void onCompleted() {
                onComplete.call();
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                onError.call(e);
            }

            @Override // rx.Observer
            public final void onNext(T args) {
                onNext.call(args);
            }
        });
    }

    public final Subscription subscribe(final Observer<? super T> observer) {
        return observer instanceof Subscriber ? subscribe((Subscriber) observer) : subscribe((Subscriber) new Subscriber<T>() { // from class: rx.Observable.30
            @Override // rx.Observer
            public void onCompleted() {
                observer.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                observer.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T t) {
                observer.onNext(t);
            }
        });
    }

    public final Subscription unsafeSubscribe(Subscriber<? super T> subscriber) {
        try {
            subscriber.onStart();
            hook.onSubscribeStart(this, this.onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            try {
                subscriber.onError(hook.onSubscribeError(e));
                return Subscriptions.unsubscribed();
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
                hook.onSubscribeError(r);
                throw r;
            }
        }
    }

    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        return subscribe(subscriber, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        }
        if (observable.onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
        }
        subscriber.onStart();
        if (!(subscriber instanceof SafeSubscriber)) {
            subscriber = new SafeSubscriber<>(subscriber);
        }
        try {
            hook.onSubscribeStart(observable, observable.onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            try {
                subscriber.onError(hook.onSubscribeError(e));
                return Subscriptions.unsubscribed();
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
                hook.onSubscribeError(r);
                throw r;
            }
        }
    }

    public final Observable<T> subscribeOn(Scheduler scheduler) {
        return this instanceof ScalarSynchronousObservable ? ((ScalarSynchronousObservable) this).scalarScheduleOn(scheduler) : (Observable<T>) nest().lift(new OperatorSubscribeOn(scheduler));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <R> Observable<R> switchMap(Func1<? super T, ? extends Observable<? extends R>> func1) {
        return switchOnNext(map(func1));
    }

    public final Observable<T> take(int i) {
        return (Observable<T>) lift(new OperatorTake(i));
    }

    public final Observable<T> take(long time, TimeUnit unit) {
        return take(time, unit, Schedulers.computation());
    }

    public final Observable<T> take(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorTakeTimed(j, timeUnit, scheduler));
    }

    public final Observable<T> takeFirst(Func1<? super T, Boolean> predicate) {
        return filter(predicate).take(1);
    }

    public final Observable<T> takeLast(int i) {
        if (i == 0) {
            return ignoreElements();
        }
        if (i == 1) {
            return (Observable<T>) lift(OperatorTakeLastOne.instance());
        }
        return (Observable<T>) lift(new OperatorTakeLast(i));
    }

    public final Observable<T> takeLast(int count, long time, TimeUnit unit) {
        return takeLast(count, time, unit, Schedulers.computation());
    }

    public final Observable<T> takeLast(int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorTakeLastTimed(i, j, timeUnit, scheduler));
    }

    public final Observable<T> takeLast(long time, TimeUnit unit) {
        return takeLast(time, unit, Schedulers.computation());
    }

    public final Observable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorTakeLastTimed(j, timeUnit, scheduler));
    }

    public final Observable<List<T>> takeLastBuffer(int count) {
        return takeLast(count).toList();
    }

    public final Observable<List<T>> takeLastBuffer(int count, long time, TimeUnit unit) {
        return takeLast(count, time, unit).toList();
    }

    public final Observable<List<T>> takeLastBuffer(int count, long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(count, time, unit, scheduler).toList();
    }

    public final Observable<List<T>> takeLastBuffer(long time, TimeUnit unit) {
        return takeLast(time, unit).toList();
    }

    public final Observable<List<T>> takeLastBuffer(long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(time, unit, scheduler).toList();
    }

    public final <E> Observable<T> takeUntil(Observable<? extends E> observable) {
        return (Observable<T>) lift(new OperatorTakeUntil(observable));
    }

    public final Observable<T> takeWhile(Func1<? super T, Boolean> func1) {
        return (Observable<T>) lift(new OperatorTakeWhile(func1));
    }

    public final Observable<T> takeUntil(Func1<? super T, Boolean> func1) {
        return (Observable<T>) lift(new OperatorTakeUntilPredicate(func1));
    }

    public final Observable<T> throttleFirst(long windowDuration, TimeUnit unit) {
        return throttleFirst(windowDuration, unit, Schedulers.computation());
    }

    public final Observable<T> throttleFirst(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorThrottleFirst(j, timeUnit, scheduler));
    }

    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit) {
        return sample(intervalDuration, unit);
    }

    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit, Scheduler scheduler) {
        return sample(intervalDuration, unit, scheduler);
    }

    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit) {
        return debounce(timeout, unit);
    }

    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return debounce(timeout, unit, scheduler);
    }

    public final Observable<TimeInterval<T>> timeInterval() {
        return timeInterval(Schedulers.immediate());
    }

    public final Observable<TimeInterval<T>> timeInterval(Scheduler scheduler) {
        return (Observable<TimeInterval<T>>) lift(new OperatorTimeInterval(scheduler));
    }

    public final <U, V> Observable<T> timeout(Func0<? extends Observable<U>> firstTimeoutSelector, Func1<? super T, ? extends Observable<V>> timeoutSelector) {
        return timeout(firstTimeoutSelector, timeoutSelector, (Observable) null);
    }

    public final <U, V> Observable<T> timeout(Func0<? extends Observable<U>> func0, Func1<? super T, ? extends Observable<V>> func1, Observable<? extends T> observable) {
        if (func1 == null) {
            throw new NullPointerException("timeoutSelector is null");
        }
        return (Observable<T>) lift(new OperatorTimeoutWithSelector(func0, func1, observable));
    }

    public final <V> Observable<T> timeout(Func1<? super T, ? extends Observable<V>> timeoutSelector) {
        return timeout((Func0) null, timeoutSelector, (Observable) null);
    }

    public final <V> Observable<T> timeout(Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        return timeout((Func0) null, timeoutSelector, other);
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout(timeout, timeUnit, null, Schedulers.computation());
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Observable<? extends T> other) {
        return timeout(timeout, timeUnit, other, Schedulers.computation());
    }

    public final Observable<T> timeout(long j, TimeUnit timeUnit, Observable<? extends T> observable, Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorTimeout(j, timeUnit, observable, scheduler));
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout(timeout, timeUnit, null, scheduler);
    }

    public final Observable<Timestamped<T>> timestamp() {
        return timestamp(Schedulers.immediate());
    }

    public final Observable<Timestamped<T>> timestamp(Scheduler scheduler) {
        return (Observable<Timestamped<T>>) lift(new OperatorTimestamp(scheduler));
    }

    public final BlockingObservable<T> toBlocking() {
        return BlockingObservable.from(this);
    }

    public final Observable<List<T>> toList() {
        return (Observable<List<T>>) lift(OperatorToObservableList.instance());
    }

    public final <K> Observable<Map<K, T>> toMap(Func1<? super T, ? extends K> func1) {
        return (Observable<Map<K, T>>) lift(new OperatorToMap(func1, UtilityFunctions.identity()));
    }

    public final <K, V> Observable<Map<K, V>> toMap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12) {
        return (Observable<Map<K, V>>) lift(new OperatorToMap(func1, func12));
    }

    public final <K, V> Observable<Map<K, V>> toMap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12, Func0<? extends Map<K, V>> func0) {
        return (Observable<Map<K, V>>) lift(new OperatorToMap(func1, func12, func0));
    }

    public final <K> Observable<Map<K, Collection<T>>> toMultimap(Func1<? super T, ? extends K> func1) {
        return (Observable<Map<K, Collection<T>>>) lift(new OperatorToMultimap(func1, UtilityFunctions.identity()));
    }

    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12) {
        return (Observable<Map<K, Collection<V>>>) lift(new OperatorToMultimap(func1, func12));
    }

    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12, Func0<? extends Map<K, Collection<V>>> func0) {
        return (Observable<Map<K, Collection<V>>>) lift(new OperatorToMultimap(func1, func12, func0));
    }

    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12, Func0<? extends Map<K, Collection<V>>> func0, Func1<? super K, ? extends Collection<V>> func13) {
        return (Observable<Map<K, Collection<V>>>) lift(new OperatorToMultimap(func1, func12, func0, func13));
    }

    public final Observable<List<T>> toSortedList() {
        return (Observable<List<T>>) lift(new OperatorToObservableSortedList(10));
    }

    public final Observable<List<T>> toSortedList(Func2<? super T, ? super T, Integer> func2) {
        return (Observable<List<T>>) lift(new OperatorToObservableSortedList(func2, 10));
    }

    @Experimental
    public final Observable<List<T>> toSortedList(int i) {
        return (Observable<List<T>>) lift(new OperatorToObservableSortedList(i));
    }

    @Experimental
    public final Observable<List<T>> toSortedList(Func2<? super T, ? super T, Integer> func2, int i) {
        return (Observable<List<T>>) lift(new OperatorToObservableSortedList(func2, i));
    }

    public final Observable<T> unsubscribeOn(Scheduler scheduler) {
        return (Observable<T>) lift(new OperatorUnsubscribeOn(scheduler));
    }

    @Experimental
    public final <U, R> Observable<R> withLatestFrom(Observable<? extends U> other, Func2<? super T, ? super U, ? extends R> resultSelector) {
        return lift(new OperatorWithLatestFrom(other, resultSelector));
    }

    public final <TClosing> Observable<Observable<T>> window(Func0<? extends Observable<? extends TClosing>> func0) {
        return (Observable<Observable<T>>) lift(new OperatorWindowWithObservableFactory(func0));
    }

    public final Observable<Observable<T>> window(int count) {
        return window(count, count);
    }

    public final Observable<Observable<T>> window(int i, int i2) {
        return (Observable<Observable<T>>) lift(new OperatorWindowWithSize(i, i2));
    }

    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit) {
        return window(timespan, timeshift, unit, PriorityMap.PRIORITY_MAX, Schedulers.computation());
    }

    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, timeshift, unit, PriorityMap.PRIORITY_MAX, scheduler);
    }

    public final Observable<Observable<T>> window(long j, long j2, TimeUnit timeUnit, int i, Scheduler scheduler) {
        return (Observable<Observable<T>>) lift(new OperatorWindowWithTime(j, j2, timeUnit, i, scheduler));
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit) {
        return window(timespan, timespan, unit, Schedulers.computation());
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, int count) {
        return window(timespan, unit, count, Schedulers.computation());
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, int count, Scheduler scheduler) {
        return window(timespan, timespan, unit, count, scheduler);
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, unit, PriorityMap.PRIORITY_MAX, scheduler);
    }

    public final <TOpening, TClosing> Observable<Observable<T>> window(Observable<? extends TOpening> observable, Func1<? super TOpening, ? extends Observable<? extends TClosing>> func1) {
        return (Observable<Observable<T>>) lift(new OperatorWindowWithStartEndObservable(observable, func1));
    }

    public final <U> Observable<Observable<T>> window(Observable<U> observable) {
        return (Observable<Observable<T>>) lift(new OperatorWindowWithObservable(observable));
    }

    public final <T2, R> Observable<R> zipWith(Iterable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return lift(new OperatorZipIterable(other, zipFunction));
    }

    public final <T2, R> Observable<R> zipWith(Observable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return zip(this, other, zipFunction);
    }

    private static class NeverObservable<T> extends Observable<T> {

        private static class Holder {
            static final NeverObservable<?> INSTANCE = new NeverObservable<>();

            private Holder() {
            }
        }

        static <T> NeverObservable<T> instance() {
            return (NeverObservable<T>) Holder.INSTANCE;
        }

        NeverObservable() {
            super(new OnSubscribe<T>() { // from class: rx.Observable.NeverObservable.1
                @Override // rx.functions.Action1
                public void call(Subscriber<? super T> observer) {
                }
            });
        }
    }

    private static class ThrowObservable<T> extends Observable<T> {
        public ThrowObservable(final Throwable exception) {
            super(new OnSubscribe<T>() { // from class: rx.Observable.ThrowObservable.1
                @Override // rx.functions.Action1
                public void call(Subscriber<? super T> observer) {
                    observer.onError(exception);
                }
            });
        }
    }
}
