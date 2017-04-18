package com.github.tehras.loanapplication.ui.home

import android.content.Context
import com.github.tehras.loanapplication.data.cache.LocalCache
import com.github.tehras.loanapplication.data.network.NetworkInteractor
import com.github.tehras.loanapplication.data.remote.LoanApiService
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.PaymentsAndLoans
import com.github.tehras.loanapplication.data.remote.models.PaymentsResponse
import com.github.tehras.loanapplication.data.request.DataObserver
import com.github.tehras.loanapplication.data.request.DataResponse
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

            var animate = loading

            subscription =
                    Observable.create<DataResponse<PaymentsAndLoans>>(DataObserver(localCache, PaymentsAndLoans::class.java, combined, networkInteractor))
                            .subscribe({
                                dataResponse ->

                                val paymentsAndLoans = dataResponse.response

                                //success
                                view?.stopLoading()
                                if (dataResponse.status == DataResponse.Status.NETWORK)
                                    view?.networkDataRetrieved()

                                paymentsAndLoans?.let {
                                    view?.updateList(it.loans, animate)
                                    view?.updateChart(it.paymentsResponse, animate)
                                    animate = false
                                }
                            }) {
                                //error
                                Timber.d("network load error cause - ${it.cause}, message - ${it.message}")
                                when (it) {
                                    is NetworkInteractor.NetworkUnavailableException -> {
                                        view?.networkDataRetrieved()
                                        view?.errorNoNetwork()
                                    }
                                    else -> {
                                        view?.errorFetchData()
                                    }
                                }
                            }

            addSubscription(subscription)
        }
    }

    override fun getLoans(showLoading: Boolean) {
        getLoans(showLoading, false)
    }

    override fun bindView(view: HomeLoanView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isDisposed) {
            view.startLoading()
        }
    }

    override fun unbindView() {
        super.unbindView()

        subscription.dispose()
    }

}
