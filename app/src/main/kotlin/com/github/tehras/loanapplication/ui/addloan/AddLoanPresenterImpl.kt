package com.github.tehras.loanapplication.ui.addloan

import com.github.tehras.loanapplication.data.network.NetworkInteractor
import com.github.tehras.loanapplication.data.remote.LoanApiService
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.ui.base.rx.RxPresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import timber.log.Timber
import javax.inject.Inject

class AddLoanPresenterImpl @Inject constructor(private val apiService: LoanApiService,
                                               private val networkInteractor: NetworkInteractor) : RxPresenter<AddLoanView>(), AddLoanPresenter {

    private var subscription: Subscription = Subscriptions.unsubscribed()

    override fun createLoan(loan: Loan) {
        Timber.d("inside create loan")
        if (subscription.isUnsubscribed) {
            Timber.d("Create Loan Called")
            view?.startLoading()

            subscription = networkInteractor.hasNetworkConnectionCompletable()
                    .andThen(apiService.submitLoan(loan)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()))
                    .subscribe({//success
                        view?.stopLoading()
                        view?.loanAddedSuccessfully()

                        subscription.unsubscribe()
                    }) {//error
                        Timber.d("cause - ${it.cause}, message - ${it.message}")
                        view?.stopLoading()
                        view?.loanCouldNotBeAdded()

                        subscription.unsubscribe()
                    }
        }

        addSubscription(subscription)
    }

    override fun bindView(view: AddLoanView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isUnsubscribed) {
            view.startLoading()
        }
    }

}