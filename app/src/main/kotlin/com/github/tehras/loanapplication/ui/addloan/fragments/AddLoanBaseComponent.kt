package com.github.tehras.loanapplication.ui.addloan.fragments

import com.github.tehras.loanapplication.ui.FragmentScope
import com.github.tehras.loanapplication.ui.addloan.fragments.balance.AddLoanBalanceFragment
import com.github.tehras.loanapplication.ui.addloan.fragments.basic.AddLoanBasicFragment
import com.github.tehras.loanapplication.ui.addloan.fragments.other.AddLoanOtherFragment
import com.github.tehras.loanapplication.ui.addloan.fragments.review.AddLoanReviewFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = arrayOf(AddLoanFragmentModule::class))
interface AddLoanBaseComponent {

    fun injectTo(fragment: AddLoanBasicFragment)
    fun injectTo(fragment: AddLoanBalanceFragment)
    fun injectTo(fragment: AddLoanOtherFragment)
    fun injectTo(fragment: AddLoanReviewFragment)
}