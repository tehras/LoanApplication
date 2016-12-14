package com.github.tehras.loanapplication.ui.login

import android.support.v7.app.AppCompatActivity
import com.github.tehras.loanapplication.ui.ActivityScope
import com.github.tehras.loanapplication.ui.base.ActivityModule
import dagger.Module
import dagger.Provides

@Module
class LoginModule(val activity: AppCompatActivity) : ActivityModule(activity) {

    @Provides @ActivityScope
    fun providePresenter(presenter: LoginPresenterImpl): LoginPresenter = presenter

}