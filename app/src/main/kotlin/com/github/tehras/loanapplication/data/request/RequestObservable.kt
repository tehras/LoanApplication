package com.github.tehras.loanapplication.data.request

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RequestObservable {

    companion object {
        fun <T> requestCachedAndNetwork(networkRequest: Observable<T>, cacheRequest: Observable<T>): Observable<T>? {
            return Observable.concat(cacheRequest, networkRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

}