package com.github.tehras.loanapplication.data.remote

import com.github.tehras.loanapplication.BuildConfig
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {
    companion object {
        var OATH_USER_KEY = if (BuildConfig.DEBUG) "DEBUG" else ""
    }

    val connectionTimeout = 5000L

    @Provides @Singleton
    fun provideApiService(retrofit: Retrofit): LoanApiService {
        return retrofit.create(LoanApiService::class.java)
    }

    @Provides @Singleton
    fun provideRetrofit(rxJavaCallAdapterFactory: RxJava2CallAdapterFactory,
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
                    .addHeader("Authorization", OATH_USER_KEY)
                    .build()

            Timber.d("Request url - ${newRequest.url()} , headers - ${newRequest.headers()}")

            val response = it.proceed(newRequest)
            Timber.d("Response retrieved - ${response.isSuccessful} - ${response.code()}")

            return@addNetworkInterceptor response
        }
                .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                .build()
    }

    @Provides @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides @Singleton
    fun provideRxJavaCallAdapter(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }
}
