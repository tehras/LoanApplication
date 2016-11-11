package com.github.tehras.loanapplication.ui.addloan

import com.github.tehras.loanapplication.data.network.NetworkInteractor
import com.github.tehras.loanapplication.data.remote.LoanApiService
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.ui.base.rx.RxPresenter
import rx.Subscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class AddLoanPresenterImpl @Inject constructor(private val apiService: LoanApiService,
                                               private val networkInteractor: NetworkInteractor) : RxPresenter<AddLoanView>(), AddLoanPresenter {

    private var subscription: Subscription = Subscriptions.unsubscribed()

    override fun createLoan(loan: Loan) {
        //todo crate a loan
    }

    override fun bindView(view: AddLoanView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isUnsubscribed) {
            view.startLoading()
        }
    }

}