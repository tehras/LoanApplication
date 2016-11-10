package com.github.tehras.loanapplication.data.network

import rx.Completable

/**
 * Created by tehras on 11/5/16.
 */
interface NetworkInteractor {
    fun hasNetworkConnection(): Boolean
    fun hasNetworkConnectionCompletable(): Completable
    class NetworkUnavailableException : Throwable("No network available!")
}
