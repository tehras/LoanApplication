package com.github.tehras.loanapplication.ui.home

import android.content.Context
import com.github.tehras.loanapplication.data.network.NetworkInteractor
import com.github.tehras.loanapplication.data.remote.LoanApiService
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.Payment
import com.github.tehras.loanapplication.data.remote.models.PaymentsResponse
import com.github.tehras.loanapplication.ui.base.rx.RxPresenter
import com.github.tehras.loanapplication.ui.loan.HomeLoanBottomSheetDialog
import rx.Subscription
import rx.subscriptions.Subscriptions
import java.util.*
import javax.inject.Inject

@Suppress("unused")
class MockHomeLoanPresenterImpl @Inject constructor(private val apiService: LoanApiService,
                                                    private val networkInteractor: NetworkInteractor) : RxPresenter<HomeLoanView>(), HomeLoanPresenter {
    override fun getLoans(showLoading: Boolean, forceNetworkCall: Boolean) {
        view?.stopLoading()
        view?.updateList(dummyLoans(), showLoading)
        view?.updateChart(dummyChartData(), showLoading)
    }

    @Inject
    lateinit var context: Context

    override fun showLoanBottomSheet(loan: Loan, activity: HomeLoanActivity) {
        activity.supportFragmentManager.beginTransaction().add(HomeLoanBottomSheetDialog.getInstance(loan), HomeLoanBottomSheetDialog::class.java.simpleName).commit()
    }

    private var subscription: Subscription = Subscriptions.unsubscribed()

    override fun getLoans(showLoading: Boolean) {
        view?.stopLoading()
        view?.updateList(dummyLoans(), showLoading)
        view?.updateChart(dummyChartData(), showLoading)
    }

    private fun dummyLoans(): ArrayList<Loan> {
        val loans: ArrayList<Loan> = ArrayList()

        loans.add(Loan("testKey", "Dummy Loan", "Dummy Provider", 104.2, 23.toDouble(), 152.23, 123.32, ""))

        return loans
    }

    private fun dummyChartData(): PaymentsResponse {
        return PaymentsResponse(ArrayList<Payment>())
    }

    override fun bindView(view: HomeLoanView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isUnsubscribed) {
            view.startLoading()
        }
    }

}
