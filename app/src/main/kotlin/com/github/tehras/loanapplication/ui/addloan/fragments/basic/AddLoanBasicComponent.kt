package com.github.tehras.loanapplication.ui.addloan.fragments.basic

import com.github.tehras.loanapplication.ui.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = arrayOf(AddLoanBasicModule::class))
interface AddLoanBasicComponent {

    fun injectTo(fragment: AddLoanBasicFragment)
}