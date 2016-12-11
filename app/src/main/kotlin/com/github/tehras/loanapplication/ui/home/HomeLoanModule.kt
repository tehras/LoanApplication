package com.github.tehras.loanapplication.ui.home

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.github.tehras.loanapplication.ApplicationQualifier
import com.github.tehras.loanapplication.ui.ActivityScope
import com.github.tehras.loanapplication.ui.base.ActivityModule
import dagger.Module
import dagger.Provides

@Module
class HomeLoanModule(val activity: AppCompatActivity) : ActivityModule(activity) {

    @Provides @ActivityScope
    fun providePresenter(presenter: HomeLoanPresenterImpl): HomeLoanPresenter = presenter
//
//    @Provides @ActivityScope
//    fun providePresenter(presenter: MockHomeLoanPresenterImpl): HomeLoanPresenter = presenter

    @Provides @ActivityScope
    fun provideLinearLayoutManager(context: Context): LinearLayoutManager = LinearLayoutManager(context)
}
