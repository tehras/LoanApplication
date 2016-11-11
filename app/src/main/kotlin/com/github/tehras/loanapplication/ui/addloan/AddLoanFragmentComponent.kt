package com.github.tehras.loanapplication.ui.addloan

import com.github.tehras.loanapplication.ui.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(AddLoanModule::class))
interface AddLoanFragmentComponent {

    fun injectTo(activity: AddLoanActivity)
}
