package com.github.tehras.loanapplication.ui.loan

import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.ui.base.Presenter

interface HomeLoanSinglePresenter : Presenter<HomeLoanSingleView> {

    fun getSingleRepayments(loan: Loan?)
    fun deleteLoan(loan: Loan?)
}