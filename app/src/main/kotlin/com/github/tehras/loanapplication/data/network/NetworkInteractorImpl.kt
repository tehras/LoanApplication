package com.github.tehras.loanapplication.data.network

import android.net.ConnectivityManager
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by tehras on 11/5/16.
 *
 * Network Interactor Implementation
 */
@Singleton
class NetworkInteractorImpl @Inject constructor(private val connectivityManager:
                                                ConnectivityManager) : NetworkInteractor {

    override fun hasNetworkConnection(): Boolean = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false

    override fun hasNetworkConnectionCompletable(): Completable {
        if (hasNetworkConnection()) {
            return Completable.complete()
        } else {
            return Completable.error { NetworkInteractor.NetworkUnavailableException() }
        }
    }

}
