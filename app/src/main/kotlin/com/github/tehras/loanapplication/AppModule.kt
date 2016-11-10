package com.github.tehras.loanapplication

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

/**
 * Created by tehras on 11/5/16.
 *
 * App Module for Dagger 2
 * This will return application
 * in order to be able to be used everywhere in the app
 */
@Module
class AppModule {

    lateinit private var application: MyApp

    constructor(application: MyApp) {
        this.application = application
    }

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideResources(): Resources = application.resources

    @Provides
    @Singleton
    @ApplicationQualifier
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideLayoutInflater(): LayoutInflater = LayoutInflater.from(application)

    @Provides
    fun provideDebugTree(): Timber.DebugTree = Timber.DebugTree()
}