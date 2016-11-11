package com.github.tehras.loanapplication.ui.addloan

import com.github.tehras.loanapplication.ui.base.MvpView

interface AddLoanView : MvpView {

    fun loanAddedSuccessfully()
    fun loanCouldNotBeAdded()

    //todo error

    fun startLoading()
    fun stopLoading()

}
