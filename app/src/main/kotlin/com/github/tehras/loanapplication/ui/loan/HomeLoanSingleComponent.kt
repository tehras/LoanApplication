package com.github.tehras.loanapplication.ui.loan

import com.github.tehras.loanapplication.ui.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = arrayOf(HomeLoanSingleModule::class))
interface HomeLoanSingleComponent {

    fun injectTo(fragment: HomeLoanBottomSheetDialog)
}