package com.github.tehras.loanapplication.ui.addloan

import com.github.tehras.loanapplication.data.network.NetworkInteractor
import com.github.tehras.loanapplication.data.remote.LoanApiService
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.ui.base.rx.RxPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
import timber.log.Timber
import javax.inject.Inject

class AddLoanPresenterImpl @Inject constructor(private val apiService: LoanApiService,
                                               private val networkInteractor: NetworkInteractor) : RxPresenter<AddLoanView>(), AddLoanPresenter {

    private var subscription: Disposable = Disposables.disposed()

    override fun createLoan(loan: Loan) {
        Timber.d("inside create loan")
        if (subscription.isDisposed) {
            Timber.d("Create Loan Called")
            view?.startLoading()

            subscription = networkInteractor.hasNetworkConnectionCompletable()
                    .andThen(apiService.submitLoan(loan)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()))
                    .subscribe({
                        //success
                        view?.stopLoading()
                        view?.loanAddedSuccessfully()

                        subscription.dispose()
                    }) {
                        //error
                        Timber.d("cause - ${it.cause}, message - ${it.message}")
                        view?.stopLoading()
                        view?.loanCouldNotBeAdded()

                        subscription.dispose()
                    }
        }

        addSubscription(subscription)
    }

    override fun bindView(view: AddLoanView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isDisposed) {
            view.startLoading()
        }
    }

}