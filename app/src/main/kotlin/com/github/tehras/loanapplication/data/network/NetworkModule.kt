package com.github.tehras.loanapplication.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.github.tehras.loanapplication.ApplicationQualifier
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by tehras on 11/5/16.
 */
@Module
class NetworkModule {

    @Provides @Singleton
    fun provideConnectivityManager(@ApplicationQualifier context: Context): ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides @Singleton
    fun provideNetworkInteractor(networkInteractorImpl: NetworkInteractorImpl): NetworkInteractor = networkInteractorImpl

}
