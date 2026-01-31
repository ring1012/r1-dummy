package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;
import okhttp3.Request;
import retrofit2.CallAdapter;

/* loaded from: classes.dex */
final class ExecutorCallAdapterFactory extends CallAdapter.Factory {
    final Executor callbackExecutor;

    ExecutorCallAdapterFactory(Executor callbackExecutor) {
        this.callbackExecutor = callbackExecutor;
    }

    @Override // retrofit2.CallAdapter.Factory
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != Call.class) {
            return null;
        }
        final Type responseType = Utils.getCallResponseType(returnType);
        return new CallAdapter<Object, Call<?>>() { // from class: retrofit2.ExecutorCallAdapterFactory.1
            @Override // retrofit2.CallAdapter
            public Type responseType() {
                return responseType;
            }

            @Override // retrofit2.CallAdapter
            public Call<?> adapt(Call<Object> call) {
                return new ExecutorCallbackCall(ExecutorCallAdapterFactory.this.callbackExecutor, call);
            }
        };
    }

    static final class ExecutorCallbackCall<T> implements Call<T> {
        final Executor callbackExecutor;
        final Call<T> delegate;

        ExecutorCallbackCall(Executor callbackExecutor, Call<T> delegate) {
            this.callbackExecutor = callbackExecutor;
            this.delegate = delegate;
        }

        @Override // retrofit2.Call
        public void enqueue(final Callback<T> callback) {
            Utils.checkNotNull(callback, "callback == null");
            this.delegate.enqueue(new Callback<T>() { // from class: retrofit2.ExecutorCallAdapterFactory.ExecutorCallbackCall.1
                @Override // retrofit2.Callback
                public void onResponse(Call<T> call, final Response<T> response) {
                    ExecutorCallbackCall.this.callbackExecutor.execute(new Runnable() { // from class: retrofit2.ExecutorCallAdapterFactory.ExecutorCallbackCall.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ExecutorCallbackCall.this.delegate.isCanceled()) {
                                callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
                            } else {
                                callback.onResponse(ExecutorCallbackCall.this, response);
                            }
                        }
                    });
                }

                @Override // retrofit2.Callback
                public void onFailure(Call<T> call, final Throwable t) {
                    ExecutorCallbackCall.this.callbackExecutor.execute(new Runnable() { // from class: retrofit2.ExecutorCallAdapterFactory.ExecutorCallbackCall.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            callback.onFailure(ExecutorCallbackCall.this, t);
                        }
                    });
                }
            });
        }

        @Override // retrofit2.Call
        public boolean isExecuted() {
            return this.delegate.isExecuted();
        }

        @Override // retrofit2.Call
        public Response<T> execute() throws IOException {
            return this.delegate.execute();
        }

        @Override // retrofit2.Call
        public void cancel() {
            this.delegate.cancel();
        }

        @Override // retrofit2.Call
        public boolean isCanceled() {
            return this.delegate.isCanceled();
        }

        @Override // retrofit2.Call
        public Call<T> clone() {
            return new ExecutorCallbackCall(this.callbackExecutor, this.delegate.clone());
        }

        @Override // retrofit2.Call
        public Request request() {
            return this.delegate.request();
        }
    }
}
