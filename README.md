# LoanApplication

package com.mobile.tiaa.cref.mvp.rx;

import com.mobile.tiaa.cref.mvp.data.home.models.base.ObservableData;
import com.mobile.tiaa.cref.mvp.data.home.network.NetworkInteractor;
import com.mobile.tiaa.cref.utils.Logger;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public abstract class DataObserver<T extends ObservableData<V>, V> implements ObservableOnSubscribe<T> {

    private T output;

    private NetworkInteractor networkInteractor;

    protected DataObserver(T t, NetworkInteractor networkInteractor) {
        this.output = t;
        this.networkInteractor = networkInteractor;
    }

    private static final String TAG = "DataObserver";

    @Override
    public void subscribe(final ObservableEmitter<T> e) throws Exception {
        Logger.d(TAG, "subscribe");
        if (!e.isDisposed()) {
            if (output.isHasNetworkDataLoaded()) {
                Logger.d(TAG, "onNext cached data");
                e.onNext(output);
            }

            if (isReadyForRefresh(output)) {
                networkInteractor.hasNetworkConnectionCompletable()
                        .andThen(getData())
                        .map(new Function<V, T>() {
                                 @Override
                                 public T apply(V v) throws Exception {
                                     output.populate(v);
                                     output.setHasNetworkDataLoaded(true);
                                     return output;
                                 }
                             }
                        )
                        .subscribe(new Consumer<T>() {
                            @Override
                            public void accept(T data) throws Exception {
                                Logger.d(TAG, "success!");

                                e.onNext(data);
                                e.onComplete();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Logger.d(TAG, "error!");

                                e.onError(throwable);
                            }
                        });
            }
        }
    }

    private boolean isReadyForRefresh(T t) {
        if (t.getLastTimeRefreshed() == 0L)
            return true;

        long minTime = t.getMinRefreshTime();

        Logger.d(TAG, "isReadyForRefresh - minTime > " + minTime + ", lastRefreshed > " + t.getLastTimeRefreshed() + ", result > " + (System.currentTimeMillis() > t.getLastTimeRefreshed() + (minTime * 1000L)));
        return System.currentTimeMillis() > t.getLastTimeRefreshed() + (minTime * 1000L);
    }

    public abstract Single<V> getData();
}
