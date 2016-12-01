package com.github.tehras.loanapplication.ui.addloan.fragments.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.extensions.*
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseComponent
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseFragment
import kotlinx.android.synthetic.main.fragment_add_loan_balance.*


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_loan_balance, container, false)
    }

    override fun commitToLoan(loan: Loan) {
        //todo
    }

    override fun animateTheViewsIn() {
        add_loan_balance_title.visibility = View.INVISIBLE
        add_loan_balance_container.visibility = View.INVISIBLE
        add_loan_balance_base_payment_container.visibility = View.INVISIBLE
        add_loan_balance_extra_payment_container.visibility = View.INVISIBLE

        add_loan_balance_balance.filterBackground(R.color.colorAccent)
        add_loan_balance_base_payment.filterBackground(R.color.colorAccent)
        add_loan_balance_extra_payment.filterBackground(R.color.colorAccent)


        add_loan_balance_balance.addTextChangedListener(AddLoanBalanceEditTextWatcher(add_loan_balance_balance))
        add_loan_balance_base_payment.addTextChangedListener(AddLoanBalanceEditTextWatcher(add_loan_balance_base_payment))
        add_loan_balance_extra_payment.addTextChangedListener(AddLoanBalanceEditTextWatcher(add_loan_balance_extra_payment))

        val animTime = context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

        add_loan_balance_title.waitForLayoutToFinish {
            animateInFromLeft(AnimationBuilder.Builder
                    .postAnimation(animTime)
                    .postAnimFunction {
                        add_loan_balance_container.animateInFromBottom(AnimationBuilder
                                .postAnimFunction {
                                    add_loan_balance_base_payment_container.animateInFromBottom(defaultAnimBuilder)
                                    add_loan_balance_extra_payment_container.animateInFromBottom(defaultAnimBuilder)
                                }
                                .build())
                    }
                    .build())
        }
    }
}