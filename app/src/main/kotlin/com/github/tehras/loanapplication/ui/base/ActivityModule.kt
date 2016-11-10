package com.github.tehras.loanapplication.ui.base

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.github.tehras.loanapplication.ui.ActivityScope
import dagger.Module
import dagger.Provides

@Module
abstract class ActivityModule(private val activity: AppCompatActivity) {

    @Provides @ActivityScope
    fun provideActivity(): AppCompatActivity = activity

    @Provides @ActivityScope
    fun provideActivityContext(): Context = activity.baseContext
}
