package com.github.tehras.loanapplication.ui.home

import android.content.Context
import com.github.tehras.loanapplication.data.network.NetworkInteractor
import com.github.tehras.loanapplication.data.remote.LoanApiService
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.PaymentsAndLoans
import com.github.tehras.loanapplication.data.remote.models.PaymentsResponse
import com.github.tehras.loanapplication.ui.base.rx.RxPresenter
import com.github.tehras.loanapplication.ui.loan.HomeLoanBottomSheetDialog
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class HomeLoanPresenterImpl @Inject constructor(private val apiService: LoanApiService,
                                                private val networkInteractor: NetworkInteractor) : RxPresenter<HomeLoanView>(), HomeLoanPresenter {

    @Inject
    lateinit var context: Context

    override fun showLoanBottomSheet(loan: Loan, activity: HomeLoanActivity) {
        activity.supportFragmentManager.beginTransaction().add(HomeLoanBottomSheetDialog.getInstance(loan), HomeLoanBottomSheetDialog::class.java.simpleName).commit()
    }

    private var subscription: Subscription = Subscriptions.unsubscribed()

    override fun getLoans(showLoading: Boolean) {
        Timber.d("trying to retrieve loans")
        if (subscription.isUnsubscribed) {
            if (showLoading)
                view?.startLoading() //show loading

            val loanSearch = apiService.loanSearch()
                    .toObservable().compose(deliverFirst<ArrayList<Loan>>())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

            val chartData = apiService.retrievePayments().toObservable().compose(deliverFirst<PaymentsResponse>())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

            subscription = networkInteractor.hasNetworkConnectionCompletable()
                    .andThen(Observable.zip(loanSearch, chartData, { loans, paymentResponse -> PaymentsAndLoans(paymentResponse, loans) }))
                    .subscribe({//success
                        view?.stopLoading()
                        view?.updateList(it.loans, showLoading)
                        view?.updateChart(it.paymentsResponse, showLoading)
                        subscription.unsubscribe()
                    }) {//error
                        when (it) {
                            is NetworkInteractor.NetworkUnavailableException -> view?.errorNoNetwork()
                            else -> view?.errorFetchData()
                        }
                        subscription.unsubscribe()
                    }

            addSubscription(subscription)
        }
    }

    override fun bindView(view: HomeLoanView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isUnsubscribed) {
            view.startLoading()
        }
    }

}
