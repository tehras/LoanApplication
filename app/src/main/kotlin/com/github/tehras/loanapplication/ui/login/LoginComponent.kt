package com.github.tehras.loanapplication.ui.login

import com.github.tehras.loanapplication.ui.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(LoginModule::class))
interface LoginComponent {

    fun injectTo(activity: LoginActivity)

}