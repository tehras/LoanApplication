package com.github.tehras.loanapplication.ui.addloan.fragments

import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.ui.base.MvpView
import com.github.tehras.loanapplication.ui.base.Presenter
import com.github.tehras.loanapplication.ui.base.PresenterFragment

abstract class AddLoanBaseFragment<V : MvpView, T : Presenter<V>> : PresenterFragment<V, T>() {
    override fun injectDependencies(graph: AppComponent) {
        injectTo(graph.plus(AddLoanFragmentModule(this)))
    }

    abstract fun injectTo(plus: AddLoanBaseComponent)

    abstract fun validateAnswers(): Boolean

}