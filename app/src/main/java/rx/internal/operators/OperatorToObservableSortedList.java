package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func2;
import rx.internal.producers.SingleDelayedProducer;

/* loaded from: classes.dex */
public final class OperatorToObservableSortedList<T> implements Observable.Operator<List<T>, T> {
    private static Comparator DEFAULT_SORT_FUNCTION = new DefaultComparableFunction();
    private final int initialCapacity;
    private final Comparator<? super T> sortFunction;

    public OperatorToObservableSortedList(int initialCapacity) {
        this.sortFunction = DEFAULT_SORT_FUNCTION;
        this.initialCapacity = initialCapacity;
    }

    public OperatorToObservableSortedList(final Func2<? super T, ? super T, Integer> func2, int i) {
        this.initialCapacity = i;
        this.sortFunction = new Comparator<T>() { // from class: rx.internal.operators.OperatorToObservableSortedList.1
            @Override // java.util.Comparator
            public int compare(T o1, T o2) {
                return ((Integer) func2.call(o1, o2)).intValue();
            }
        };
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super List<T>> subscriber) {
        final SingleDelayedProducer singleDelayedProducer = new SingleDelayedProducer(subscriber);
        Subscriber<T> subscriber2 = new Subscriber<T>() { // from class: rx.internal.operators.OperatorToObservableSortedList.2
            boolean completed;
            List<T> list;

            {
                this.list = new ArrayList(OperatorToObservableSortedList.this.initialCapacity);
            }

            @Override // rx.Subscriber
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            @Override // rx.Observer
            public void onCompleted() {
                if (!this.completed) {
                    this.completed = true;
                    List<T> a2 = this.list;
                    this.list = null;
                    try {
                        Collections.sort(a2, OperatorToObservableSortedList.this.sortFunction);
                        singleDelayedProducer.setValue(a2);
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, this);
                    }
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T value) {
                if (!this.completed) {
                    this.list.add(value);
                }
            }
        };
        subscriber.add(subscriber2);
        subscriber.setProducer(singleDelayedProducer);
        return subscriber2;
    }

    private static class DefaultComparableFunction implements Comparator<Object> {
        private DefaultComparableFunction() {
        }

        @Override // java.util.Comparator
        public int compare(Object t1, Object t2) {
            Comparable<Object> c1 = (Comparable) t1;
            Comparable<Object> c2 = (Comparable) t2;
            return c1.compareTo(c2);
        }
    }
}
