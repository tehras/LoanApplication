package com.github.tehras.loanapplication.ui.addloan.fragments

import android.os.Bundle
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.ui.base.MvpView
import com.github.tehras.loanapplication.ui.base.Presenter
import com.github.tehras.loanapplication.ui.base.PresenterFragment
import timber.log.Timber

abstract class AddLoanBaseFragment<V : MvpView, T : Presenter<V>> : PresenterFragment<V, T>() {
    var loan: Loan? = null
    var prePopulate = false

    companion object {
        val ARG_LOAN = "loan_argument"
        val ARG_PRE_POPULATE = "pre_populate_arg"

        fun injectLoan(loan: Loan, args: Bundle) {
            args.putParcelable(ARG_LOAN, loan)
            args.putBoolean(ARG_PRE_POPULATE, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            Timber.d("getting arg - $prePopulate, $loan")
            prePopulate = arguments?.getBoolean(ARG_PRE_POPULATE) ?: false
            loan = arguments?.getParcelable(ARG_LOAN)
        }
    }

    override fun injectDependencies(graph: AppComponent) {
        injectTo(graph.plus(AddLoanFragmentModule(this)))
    }

    override fun onStart() {
        super.onStart()

        //animate the textViews once visible
        animateTheViewsIn()
    }

    abstract fun animateTheViewsIn()

    abstract fun injectTo(plus: AddLoanBaseComponent)

    abstract fun validateAnswers(): Boolean

    abstract fun commitToLoan(loan: Loan)
}