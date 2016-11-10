package com.github.tehras.loanapplication.ui.home

import com.github.tehras.loanapplication.ui.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(HomeLoanModule::class))
interface HomeLoanComponent {

    fun injectTo(activity: HomeLoanActivity)
}