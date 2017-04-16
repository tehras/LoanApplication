package com.github.tehras.loanapplication.ui.loan

import com.github.tehras.loanapplication.data.network.NetworkInteractor
import com.github.tehras.loanapplication.data.remote.LoanApiService
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.ui.base.rx.RxPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class HomeLoanSinglePresenterImpl @Inject constructor(private val apiService: LoanApiService,
                                                      private val networkInteractor: NetworkInteractor) : RxPresenter<HomeLoanSingleView>(), HomeLoanSinglePresenter {

    private var subscription: Disposable = Disposables.empty()
    private var deleteSub: Disposable = Disposables.empty()

    override fun deleteLoan(loan: Loan?) {
        if (deleteSub.isDisposed) {
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
        if (subscription.isDisposed) {
            Timber.d("trying to retrieve single repayments")

            view?.startChartLoading() //show loading

            subscription = networkInteractor.hasNetworkConnectionCompletable()
                    .andThen(apiService.retrieveSingleRepayments(loanId = loan?.key ?: "")
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()))
                    .subscribe({
                        //success
                        Timber.d("success ${it[0].payments?.size}")
                        view?.stopChartLoading()
                        view?.updateChart(it)

                        subscription.dispose()
                    }) {
                        //error
                        view?.stopChartLoading()
                        when (it) {
                            is NetworkInteractor.NetworkUnavailableException -> view?.errorNoNetwork()
                            else -> view?.errorFetchData()
                        }

                        subscription.dispose()
                    }
        }

        addSubscription(subscription)
    }

    override fun bindView(view: HomeLoanSingleView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isDisposed) {
            view.startChartLoading()
        }
    }
}