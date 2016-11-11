package com.github.tehras.loanapplication.ui.home

import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.ui.base.Presenter

interface HomeLoanPresenter : Presenter<HomeLoanView> {

    fun getLoans(showLoading: Boolean)
    fun showLoanBottomSheet(loan: Loan, activity: HomeLoanActivity)

}
