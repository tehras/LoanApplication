package com.github.tehras.loanapplication.ui.home

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.github.tehras.loanapplication.ui.ActivityScope
import com.github.tehras.loanapplication.ui.FragmentScope
import com.github.tehras.loanapplication.ui.base.ActivityModule
import com.github.tehras.loanapplication.ui.loan.HomeLoanSinglePresenter
import com.github.tehras.loanapplication.ui.loan.HomeLoanSinglePresenterImpl
import dagger.Module
import dagger.Provides

@Module
class HomeLoanModule(activity: AppCompatActivity) : ActivityModule(activity) {

    @Provides @ActivityScope
    fun providePresenter(presenter: HomeLoanPresenterImpl): HomeLoanPresenter = presenter

    @Provides @FragmentScope
    fun provideFragmentPresenter(presenter: HomeLoanSinglePresenterImpl): HomeLoanSinglePresenter = presenter

    @Provides @ActivityScope
    fun provideLinearLayoutManager(context: Context): LinearLayoutManager = LinearLayoutManager(context)
}
