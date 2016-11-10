package com.github.tehras.loanapplication.ui.loan

import android.support.v4.app.Fragment
import com.github.tehras.loanapplication.ui.FragmentScope
import com.github.tehras.loanapplication.ui.base.FragmentModule
import dagger.Module
import dagger.Provides

@Module
class HomeLoanSingleModule(fragment: Fragment) : FragmentModule(fragment = fragment) {

    @Provides @FragmentScope
    fun providePresenter(presenter: HomeLoanSinglePresenterImpl): HomeLoanSinglePresenter = presenter
}
