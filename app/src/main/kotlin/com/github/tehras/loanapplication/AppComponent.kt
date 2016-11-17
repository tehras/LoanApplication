package com.github.tehras.loanapplication

import com.github.tehras.loanapplication.data.network.NetworkModule
import com.github.tehras.loanapplication.data.remote.ApiModule
import com.github.tehras.loanapplication.ui.addloan.AddLoanComponent
import com.github.tehras.loanapplication.ui.addloan.AddLoanModule
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseComponent
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanFragmentModule
import com.github.tehras.loanapplication.ui.home.HomeLoanComponent
import com.github.tehras.loanapplication.ui.home.HomeLoanModule
import com.github.tehras.loanapplication.ui.loan.HomeLoanSingleComponent
import com.github.tehras.loanapplication.ui.loan.HomeLoanSingleModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by tehras on 11/5/16.
 *
 * AppComponent will insert the appropriate modules
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, ApiModule::class))
interface AppComponent {

    fun injectTo(app: MyApp)

    // Submodule methods
    // Every screen is its own submodule of the graph and must be added here.
    // fun plus(module: Module): ModuleComponent

    fun plus(homeLoanModule: HomeLoanModule): HomeLoanComponent
    fun plus(addLoanModule: AddLoanModule): AddLoanComponent
    fun plus(homeSingleLoanModule: HomeLoanSingleModule): HomeLoanSingleComponent

    fun plus(addLoanBasicModule: AddLoanFragmentModule): AddLoanBaseComponent
}
