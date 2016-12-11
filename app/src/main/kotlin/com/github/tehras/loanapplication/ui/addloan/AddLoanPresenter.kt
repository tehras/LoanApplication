package com.github.tehras.loanapplication.ui.addloan

import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.ui.base.Presenter

interface AddLoanPresenter : Presenter<AddLoanView> {

    fun createLoan(loan: Loan)

}