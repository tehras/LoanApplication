package com.github.tehras.loanapplication.ui.addloan.fragments.basic

import android.support.v4.app.Fragment
import com.github.tehras.loanapplication.ui.FragmentScope
import com.github.tehras.loanapplication.ui.base.FragmentModule
import dagger.Module
import dagger.Provides

@Module
class AddLoanBasicModule(fragment: Fragment) : FragmentModule(fragment = fragment) {

    @Provides @FragmentScope
    fun providePresenter(presenter: AddLoanBasicPresenterImpl): AddLoanBasicPresenter = presenter
}
