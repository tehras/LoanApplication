package com.github.tehras.loanapplication.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.github.tehras.loanapplication.ApplicationQualifier
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalCacheModule() {

    @Provides @Singleton
    fun providesSharedPreferences(@ApplicationQualifier context: Context): SharedPreferences =
            context.getSharedPreferences(LocalDataManagerImpl.DATA_CACHE_KEY, Context.MODE_PRIVATE) ?: throw NoLocalDataException()

    @Provides @Singleton
    fun provideLocalCache(localCacheImpl: LocalCacheImpl): LocalCache = localCacheImpl

    @Provides @Singleton
    fun provideDataManager(localDataManagerImpl: LocalDataManagerImpl): LocalDataManager = localDataManagerImpl

}