package com.github.tehras.loanapplication.ui.loan

import com.github.tehras.loanapplication.data.network.NetworkInteractor
import com.github.tehras.loanapplication.data.remote.LoanApiService
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.SinglePaymentResponse
import com.github.tehras.loanapplication.ui.base.rx.RxPresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class HomeLoanSinglePresenterImpl @Inject constructor(private val apiService: LoanApiService,
                                                      private val networkInteractor: NetworkInteractor) : RxPresenter<HomeLoanSingleView>(), HomeLoanSinglePresenter {

    private var subscription: Subscription = Subscriptions.unsubscribed()
    private var deleteSub: Subscription = Subscriptions.unsubscribed()

    override fun deleteLoan(loan: Loan?) {
        if (
        deleteSub.isUnsubscribed) {
            view?.startDeleteLoading()

            deleteSub = networkInteractor.hasNetworkConnectionCompletable()
                    .andThen(apiService.deleteLoan(loanId = loan?.key ?: "")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()))
                    .subscribe({
                        view?.successDeletingLoan()
                        view?.stopDeleteLoading()
                    }) {
                        view?.stopDeleteLoading()
                        view?.errorDeletingLoan()
                    }
        }
    }

    override fun getSingleRepayments(loan: Loan?) {
        if (subscription.isUnsubscribed) {
            Timber.d("trying to retrieve single repayments")

            view?.startChartLoading() //show loading

            subscription = networkInteractor.hasNetworkConnectionCompletable()
                    .andThen(apiService.retrieveSingleRepayments(loanId = loan?.key ?: "")
                            .toObservable().compose(deliverFirst<ArrayList<SinglePaymentResponse>>())
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()))
                    .subscribe({//success
                        Timber.d("success ${it[0].payments?.size}")
                        view?.stopChartLoading()
                        view?.updateChart(it)

                        subscription.unsubscribe()
                    }) {//error
                        view?.stopChartLoading()
                        when (it) {
                            is NetworkInteractor.NetworkUnavailableException -> view?.errorNoNetwork()
                            else -> view?.errorFetchData()
                        }

                        subscription.unsubscribe()
                    }
        }

        addSubscription(subscription)
    }

    override fun bindView(view: HomeLoanSingleView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isUnsubscribed) {
            view.startChartLoading()
        }
    }
}