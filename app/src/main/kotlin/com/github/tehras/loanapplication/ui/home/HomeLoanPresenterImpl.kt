package com.github.tehras.loanapplication.ui.home

import android.content.Context
import com.github.tehras.loanapplication.data.cache.CacheConstants
import com.github.tehras.loanapplication.data.cache.LocalCache
import com.github.tehras.loanapplication.data.network.NetworkInteractor
import com.github.tehras.loanapplication.data.remote.LoanApiService
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.PaymentsAndLoans
import com.github.tehras.loanapplication.data.remote.models.PaymentsResponse
import com.github.tehras.loanapplication.data.request.RequestObservable
import com.github.tehras.loanapplication.ui.base.rx.RxPresenter
import com.github.tehras.loanapplication.ui.loan.HomeLoanBottomSheetDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
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

    private var subscription: Disposable = Disposables.disposed()

    override fun getLoans(showLoading: Boolean, forceNetworkCall: Boolean) {
        val loading = showLoading
        Timber.d("trying to retrieve loans")
//        if (!forceNetworkCall) {
//            localSub.dispose()
//            localSub = localCache.isCached(CacheConstants.LOAN_AND_PAYMENTS_KEY)
//                    .andThen(localCache.get(CacheConstants.LOAN_AND_PAYMENTS_KEY, PaymentsAndLoans::class.java)
//                            .compose(deliverFirst<PaymentsAndLoans>())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread()))
//                    .subscribe({
//                        Timber.d("loans retrieved from local loan")
//                        loading = false
//
//                        view?.stopLoading()
//                        view?.updateList(it.loans, showLoading)
//                        view?.updateChart(it.paymentsResponse, showLoading)
//                        view?.localDataRetrieved()
//                        localSub.unsubscribe()
//                    }) {
//                        Timber.d("error thrown")
//                    }
//
//            addSubscription(localSub)
//        }

        if (subscription.isDisposed) {
            if (showLoading)
                view?.startLoading() //show loading

            val combined = Observable.zip(
                    apiService
                            .loanSearch()
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()),
                    apiService
                            .retrievePayments()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .toObservable(),
                    BiFunction<ArrayList<Loan>, PaymentsResponse, PaymentsAndLoans> { loans, payments ->
                        return@BiFunction PaymentsAndLoans(loans = loans, paymentsResponse = payments)
                    })

            subscription =
                    networkInteractor.hasNetworkConnectionCompletable()
                            .andThen(RequestObservable.requestCachedAndNetwork(combined, readFromCache()))
                            .subscribe({
                                //success
                                Timber.d("loans retrieved from network loan - $loading")
                                view?.stopLoading()
                                view?.networkDataRetrieved()
                                view?.updateList(it.loans, loading)
                                view?.updateChart(it.paymentsResponse, loading)

                                saveDataToCache(it)
                            }) {
                                //error
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
                            }

            addSubscription(subscription)
        }
    }

    private fun readFromCache(): Observable<PaymentsAndLoans> {
        return localCache.isCached(CacheConstants.LOAN_AND_PAYMENTS_KEY)
                .andThen(localCache.get(CacheConstants.LOAN_AND_PAYMENTS_KEY, PaymentsAndLoans::class.java)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
    }

    override fun getLoans(showLoading: Boolean) {
        getLoans(showLoading, false)
    }

    private fun saveDataToCache(it: PaymentsAndLoans) {
        localCache.save(CacheConstants.LOAN_AND_PAYMENTS_KEY, it)
                .subscribeOn(Schedulers.io()).subscribe()
    }

    private fun clearDataFromCache() {
        localCache.delete(CacheConstants.LOAN_AND_PAYMENTS_KEY).subscribeOn(Schedulers.io()).subscribe()
    }

    override fun bindView(view: HomeLoanView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isDisposed) {
            view.startLoading()
        }
    }

}
