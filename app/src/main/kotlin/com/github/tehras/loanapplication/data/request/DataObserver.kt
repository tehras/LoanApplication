package com.github.tehras.loanapplication.data.request


import com.github.tehras.loanapplication.data.cache.CacheConstants
import com.github.tehras.loanapplication.data.cache.LocalCache
import com.github.tehras.loanapplication.data.network.NetworkInteractor
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class DataObserver<T> @Inject constructor(val localCache: LocalCache, val clazz: Class<T>, val networkObservable: Observable<T>, val networkInteractor: NetworkInteractor) : ObservableOnSubscribe<DataResponse<T>> {
    var dataResponse: DataResponse<T> = DataResponse()

    override fun subscribe(e: ObservableEmitter<DataResponse<T>>?) {
        Timber.d("subscribe")
        e?.let {
            if (!it.isDisposed) {
                networkInteractor.hasNetworkConnectionCompletable()
                        .andThen(Observable.concat(readFromCache(), readFromNetwork()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ data ->
                            Timber.d("response came back successfully")

                            e.onNext(data)
                        }, { throwable ->
                            Timber.d("error - $throwable")

                            e.onError(throwable)
                        })
            }
        }
    }

    private fun saveToCache(o: T): Unit {
        localCache.save(CacheConstants.LOAN_AND_PAYMENTS_KEY, o)
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    private fun readFromNetwork(): Observable<DataResponse<T>> {
        return networkObservable.map {
            v ->
            Timber.d("readFromNetwork mapping")
            saveToCache(v)
            dataResponse.response = v
            dataResponse.status = DataResponse.Status.NETWORK

            return@map dataResponse
        }
    }

    private fun readFromCache(): Observable<DataResponse<T>> {
        return localCache.isCached(CacheConstants.LOAN_AND_PAYMENTS_KEY)
                .andThen(localCache.get(CacheConstants.LOAN_AND_PAYMENTS_KEY, clazz))
                .doAfterNext { Timber.d("Loaded Cache") }
                .map { v ->
                    dataResponse.response = v
                    dataResponse.status = DataResponse.Status.CACHE

                    return@map dataResponse
                }
                .doOnError { error ->
                    Timber.d("error loading cache $error")
                }
    }
}