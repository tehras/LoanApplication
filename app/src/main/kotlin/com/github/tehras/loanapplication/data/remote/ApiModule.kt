package com.github.tehras.loanapplication.data.remote

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

/**
 * Created by tehras on 11/5/16.
 */
@Module
class ApiModule {
    @Provides @Singleton
    fun provideApiService(retrofit: Retrofit): LoanApiService {
        return retrofit.create(LoanApiService::class.java)
    }

    @Provides @Singleton
    fun provideRetrofit(rxJavaCallAdapterFactory: RxJavaCallAdapterFactory,
                        gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(ApiConstants.LOAN_BASE_URL)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient())
                .build()
    }

    private fun okHttpClient(): OkHttpClient? {
        return OkHttpClient.Builder().addNetworkInterceptor {
            val newRequest = it.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "DEBUG")
                    .build()

            Timber.d("Request url - ${newRequest.url()} , headers - ${newRequest.headers()}")

            val response = it.proceed(newRequest)
            Timber.d("Response retrieved - ${response.isSuccessful} - ${response.code()}")

            return@addNetworkInterceptor response
        }.build()
    }

    @Provides @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides @Singleton
    fun provideRxJavaCallAdapter(): RxJavaCallAdapterFactory {
        return RxJavaCallAdapterFactory.create()
    }
}
