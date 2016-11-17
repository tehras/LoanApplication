package com.github.tehras.loanapplication.ui.addloan.fragments.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.extensions.addToBundle
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseComponent
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseFragment


class AddLoanBalanceFragment : AddLoanBaseFragment<AddLoanBalanceView, AddLoanBalancePresenter>(), AddLoanBalanceView {
    override fun injectTo(plus: AddLoanBaseComponent) {
        plus.injectTo(this)
    }

    override fun validateAnswers(): Boolean {
        //todo valid answers
        return true
    }

    companion object {
        val ARG_LOAN = "arg_loan_parcelable"

        fun instance(loan: Loan): AddLoanBalanceFragment {
            return AddLoanBalanceFragment().addToBundle { putParcelable(ARG_LOAN, loan) }
        }
    }

    lateinit var loan: Loan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            loan = arguments.getParcelable(ARG_LOAN)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //TODO populate the view

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}