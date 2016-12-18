package com.github.tehras.loanapplication.ui.home

import android.content.Context
import com.github.tehras.loanapplication.data.cache.CacheConstants
import com.github.tehras.loanapplication.data.cache.LocalCache
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
    @Inject
    lateinit var localCache: LocalCache

    override fun showLoanBottomSheet(loan: Loan, activity: HomeLoanActivity) {
        activity.supportFragmentManager.beginTransaction().add(HomeLoanBottomSheetDialog.getInstance(loan), HomeLoanBottomSheetDialog::class.java.simpleName).commit()
    }

    private var subscription: Subscription = Subscriptions.unsubscribed()
    private var localSub: Subscription = Subscriptions.unsubscribed()

    override fun getLoans(showLoading: Boolean, forceNetworkCall: Boolean) {
        var loading = showLoading
        Timber.d("trying to retrieve loans")
        if (!forceNetworkCall) {
            localSub.unsubscribe()
            localSub = localCache.isCached(CacheConstants.LOAN_AND_PAYMENTS_KEY)
                    .andThen(localCache.get(CacheConstants.LOAN_AND_PAYMENTS_KEY, PaymentsAndLoans::class.java)
                            .compose(deliverFirst<PaymentsAndLoans>())
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()))
                    .subscribe({
                        Timber.d("loans retrieved from local loan")
                        loading = false

                        view?.stopLoading()
                        view?.updateList(it.loans, showLoading)
                        view?.updateChart(it.paymentsResponse, showLoading)
                        view?.localDataRetrieved()
                        localSub.unsubscribe()
                    }) {
                        Timber.d("error thrown")
                    }

            addSubscription(localSub)
        }

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
                        Timber.d("loans retrieved from network loan - $loading")
                        view?.stopLoading()
                        view?.networkDataRetrieved()
                        view?.updateList(it.loans, loading)
                        view?.updateChart(it.paymentsResponse, loading)
                        subscription.unsubscribe()

                        saveDataToCache(it)
                    }) {//error
                        Timber.d("network load error cause - ${it.cause}, message - ${it.message}")
                        when (it) {
                            is NetworkInteractor.NetworkUnavailableException -> {
                                view?.networkDataRetrieved()
                                view?.errorNoNetwork()
                            }
                            else -> {
                                clearDataFromCache()
                                view?.errorFetchData()
                            }
                        }
                        subscription.unsubscribe()
                    }

            addSubscription(subscription)
        }
    }

    override fun getLoans(showLoading: Boolean) {
        getLoans(showLoading, false)
    }

    private fun saveDataToCache(it: PaymentsAndLoans) {
        localSub.unsubscribe()
        localSub = localCache.save(CacheConstants.LOAN_AND_PAYMENTS_KEY, it)
                .subscribeOn(Schedulers.io()).subscribe()

        addSubscription(localSub)
    }

    private fun clearDataFromCache() {
        localSub.unsubscribe()
        localSub = localCache.delete(CacheConstants.LOAN_AND_PAYMENTS_KEY)
                .subscribeOn(Schedulers.io()).subscribe()
    }

    override fun bindView(view: HomeLoanView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isUnsubscribed) {
            view.startLoading()
        }
    }

}
