package rx.observers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import rx.Notification;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.CompositeException;

/* loaded from: classes.dex */
public class TestSubscriber<T> extends Subscriber<T> {
    private static final Observer<Object> INERT = new Observer<Object>() { // from class: rx.observers.TestSubscriber.1
        @Override // rx.Observer
        public void onCompleted() {
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
        }

        @Override // rx.Observer
        public void onNext(Object t) {
        }
    };
    private final long initialRequest;
    private volatile Thread lastSeenThread;
    private final CountDownLatch latch;
    private final TestObserver<T> testObserver;

    public TestSubscriber(long initialRequest) {
        this(INERT, initialRequest);
    }

    public TestSubscriber(Observer<T> delegate, long initialRequest) {
        this.latch = new CountDownLatch(1);
        if (delegate == null) {
            throw new NullPointerException();
        }
        this.testObserver = new TestObserver<>(delegate);
        this.initialRequest = initialRequest;
    }

    public TestSubscriber(Subscriber<T> delegate) {
        this(delegate, -1L);
    }

    public TestSubscriber(Observer<T> delegate) {
        this(delegate, -1L);
    }

    public TestSubscriber() {
        this(-1L);
    }

    public static <T> TestSubscriber<T> create() {
        return new TestSubscriber<>();
    }

    public static <T> TestSubscriber<T> create(long initialRequest) {
        return new TestSubscriber<>(initialRequest);
    }

    public static <T> TestSubscriber<T> create(Observer<T> delegate, long initialRequest) {
        return new TestSubscriber<>(delegate, initialRequest);
    }

    public static <T> TestSubscriber<T> create(Subscriber<T> delegate) {
        return new TestSubscriber<>((Subscriber) delegate);
    }

    public static <T> TestSubscriber<T> create(Observer<T> delegate) {
        return new TestSubscriber<>(delegate);
    }

    @Override // rx.Subscriber
    public void onStart() {
        if (this.initialRequest >= 0) {
            requestMore(this.initialRequest);
        }
    }

    @Override // rx.Observer
    public void onCompleted() {
        try {
            this.lastSeenThread = Thread.currentThread();
            this.testObserver.onCompleted();
        } finally {
            this.latch.countDown();
        }
    }

    public List<Notification<T>> getOnCompletedEvents() {
        return this.testObserver.getOnCompletedEvents();
    }

    @Override // rx.Observer
    public void onError(Throwable e) {
        try {
            this.lastSeenThread = Thread.currentThread();
            this.testObserver.onError(e);
        } finally {
            this.latch.countDown();
        }
    }

    public List<Throwable> getOnErrorEvents() {
        return this.testObserver.getOnErrorEvents();
    }

    @Override // rx.Observer
    public void onNext(T t) {
        this.lastSeenThread = Thread.currentThread();
        this.testObserver.onNext(t);
    }

    public void requestMore(long n) {
        request(n);
    }

    public List<T> getOnNextEvents() {
        return this.testObserver.getOnNextEvents();
    }

    public void assertReceivedOnNext(List<T> items) {
        this.testObserver.assertReceivedOnNext(items);
    }

    public void assertTerminalEvent() {
        this.testObserver.assertTerminalEvent();
    }

    public void assertUnsubscribed() {
        if (!isUnsubscribed()) {
            throw new AssertionError("Not unsubscribed.");
        }
    }

    public void assertNoErrors() {
        List<Throwable> onErrorEvents = getOnErrorEvents();
        if (onErrorEvents.size() > 0) {
            AssertionError ae = new AssertionError("Unexpected onError events: " + getOnErrorEvents().size());
            if (onErrorEvents.size() == 1) {
                ae.initCause(getOnErrorEvents().get(0));
                throw ae;
            }
            ae.initCause(new CompositeException(onErrorEvents));
            throw ae;
        }
    }

    public void awaitTerminalEvent() throws InterruptedException {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    public void awaitTerminalEvent(long timeout, TimeUnit unit) throws InterruptedException {
        try {
            this.latch.await(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    public void awaitTerminalEventAndUnsubscribeOnTimeout(long timeout, TimeUnit unit) throws InterruptedException {
        try {
            boolean result = this.latch.await(timeout, unit);
            if (!result) {
                unsubscribe();
            }
        } catch (InterruptedException e) {
            unsubscribe();
        }
    }

    public Thread getLastSeenThread() {
        return this.lastSeenThread;
    }

    public void assertCompleted() {
        int s = this.testObserver.getOnCompletedEvents().size();
        if (s == 0) {
            throw new AssertionError("Not completed!");
        }
        if (s > 1) {
            throw new AssertionError("Completed multiple times: " + s);
        }
    }

    public void assertNotCompleted() {
        int s = this.testObserver.getOnCompletedEvents().size();
        if (s == 1) {
            throw new AssertionError("Completed!");
        }
        if (s > 1) {
            throw new AssertionError("Completed multiple times: " + s);
        }
    }

    public void assertError(Class<? extends Throwable> clazz) {
        List<Throwable> err = this.testObserver.getOnErrorEvents();
        if (err.size() == 0) {
            throw new AssertionError("No errors");
        }
        if (err.size() > 1) {
            AssertionError ae = new AssertionError("Multiple errors: " + err.size());
            ae.initCause(new CompositeException(err));
            throw ae;
        }
        if (!clazz.isInstance(err.get(0))) {
            AssertionError ae2 = new AssertionError("Exceptions differ; expected: " + clazz + ", actual: " + err.get(0));
            ae2.initCause(err.get(0));
            throw ae2;
        }
    }

    public void assertError(Throwable throwable) {
        List<Throwable> err = this.testObserver.getOnErrorEvents();
        if (err.size() == 0) {
            throw new AssertionError("No errors");
        }
        if (err.size() > 1) {
            AssertionError ae = new AssertionError("Multiple errors: " + err.size());
            ae.initCause(new CompositeException(err));
            throw ae;
        }
        if (!throwable.equals(err.get(0))) {
            AssertionError ae2 = new AssertionError("Exceptions differ; expected: " + throwable + ", actual: " + err.get(0));
            ae2.initCause(err.get(0));
            throw ae2;
        }
    }

    public void assertNoTerminalEvent() {
        List<Throwable> err = this.testObserver.getOnErrorEvents();
        int s = this.testObserver.getOnCompletedEvents().size();
        if (err.size() > 0 || s > 0) {
            if (err.isEmpty()) {
                throw new AssertionError("Found " + err.size() + " errors and " + s + " completion events instead of none");
            }
            if (err.size() == 1) {
                AssertionError ae = new AssertionError("Found " + err.size() + " errors and " + s + " completion events instead of none");
                ae.initCause(err.get(0));
                throw ae;
            }
            AssertionError ae2 = new AssertionError("Found " + err.size() + " errors and " + s + " completion events instead of none");
            ae2.initCause(new CompositeException(err));
            throw ae2;
        }
    }

    public void assertNoValues() {
        int s = this.testObserver.getOnNextEvents().size();
        if (s > 0) {
            throw new AssertionError("No onNext events expected yet some received: " + s);
        }
    }

    public void assertValueCount(int count) {
        int s = this.testObserver.getOnNextEvents().size();
        if (s != count) {
            throw new AssertionError("Number of onNext events differ; expected: " + count + ", actual: " + s);
        }
    }

    public void assertValues(T... values) {
        assertReceivedOnNext(Arrays.asList(values));
    }

    public void assertValue(T value) {
        assertReceivedOnNext(Collections.singletonList(value));
    }
}
