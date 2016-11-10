package com.github.tehras.loanapplication.ui.loan

import com.github.tehras.loanapplication.data.remote.models.SinglePaymentResponse
import com.github.tehras.loanapplication.ui.base.MvpView
import java.util.*

interface HomeLoanSingleView : MvpView {

    fun updateChart(payments: ArrayList<SinglePaymentResponse>)

    fun startChartLoading()
    fun stopChartLoading()

    fun errorNoNetwork()
    fun errorFetchData()

}