package com.github.tehras.loanapplication.ui.addloan.fragments

import android.support.v4.app.Fragment
import com.github.tehras.loanapplication.ui.FragmentScope
import com.github.tehras.loanapplication.ui.addloan.fragments.balance.AddLoanBalancePresenter
import com.github.tehras.loanapplication.ui.addloan.fragments.balance.AddLoanBalancePresenterImpl
import com.github.tehras.loanapplication.ui.addloan.fragments.basic.AddLoanBasicPresenter
import com.github.tehras.loanapplication.ui.addloan.fragments.basic.AddLoanBasicPresenterImpl
import com.github.tehras.loanapplication.ui.base.FragmentModule
import dagger.Module
import dagger.Provides

@Module
@FragmentScope
class AddLoanFragmentModule(fragment: Fragment) : FragmentModule(fragment) {

    @Provides @FragmentScope
    fun providesBasicProvider(presenter: AddLoanBasicPresenterImpl): AddLoanBasicPresenter = presenter

    @Provides @FragmentScope
    fun providesBalanceProvider(presenter: AddLoanBalancePresenterImpl): AddLoanBalancePresenter = presenter
}
