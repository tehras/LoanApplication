package com.github.tehras.loanapplication.ui.addloan

import android.support.v7.app.AppCompatActivity
import com.github.tehras.loanapplication.ui.ActivityScope
import com.github.tehras.loanapplication.ui.base.ActivityModule
import dagger.Module
import dagger.Provides

@Module
class AddLoanModule(activity: AppCompatActivity) : ActivityModule(activity) {

    @Provides @ActivityScope
    fun providePresenter(presenter: AddLoanPresenterImpl): AddLoanPresenter = presenter

}