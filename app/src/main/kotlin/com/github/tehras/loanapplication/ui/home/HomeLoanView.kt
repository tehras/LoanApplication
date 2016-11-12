package com.github.tehras.loanapplication.ui.home

import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.PaymentsResponse
import com.github.tehras.loanapplication.ui.base.MvpView
import java.util.*

interface HomeLoanView : MvpView {
    fun updateList(loans: ArrayList<Loan>)
    fun updateChart(payments: PaymentsResponse, animate: Boolean)

    fun startLoading()
    fun stopLoading()

    fun errorNoNetwork()
    fun errorFetchData()
}