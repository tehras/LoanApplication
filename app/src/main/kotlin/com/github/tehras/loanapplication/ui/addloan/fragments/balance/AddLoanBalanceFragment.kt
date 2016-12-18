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
        return (isValidBalance() && isValidBasePayment() && isValidExtraPayment())
    }

    private fun isValidExtraPayment(): Boolean {
        return add_loan_balance_extra_payment.isValidAmount(0.00, add_loan_balance_balance.convertToDouble() - add_loan_balance_base_payment.convertToDouble())
    }

    private fun isValidBalance(): Boolean {
        return add_loan_balance_balance.isValidAmount(0.01, 999999.99)
    }

    private fun isValidBasePayment(): Boolean {
        return add_loan_balance_base_payment.isValidAmount(0.01, add_loan_balance_balance.convertToDouble())
    }

    companion object {

        fun instance(): AddLoanBalanceFragment {
            return AddLoanBalanceFragment()
        }

        fun instance(loan: Loan, populate: Boolean): AddLoanBalanceFragment {
            return AddLoanBalanceFragment().addToBundle { if (populate) injectLoan(loan, this) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_loan_balance, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (prePopulate) {
            add_loan_balance_balance.setText(loan?.balance?.dollarWithTwoDecimalsFormat())
            add_loan_balance_base_payment.setText(loan?.payment?.dollarWithTwoDecimalsFormat())
            add_loan_balance_extra_payment.setText(loan?.extraPayment?.dollarWithTwoDecimalsFormat())
        }

        add_loan_balance_balance.addErrorTextWatcher { isValidBalance() }
        add_loan_balance_base_payment.addErrorTextWatcher { isValidBasePayment() }
        add_loan_balance_extra_payment.addErrorTextWatcher { isValidExtraPayment() }

        add_loan_balance_balance.filterBackground(R.color.colorAccent)
        add_loan_balance_base_payment.filterBackground(R.color.colorAccent)
        add_loan_balance_extra_payment.filterBackground(R.color.colorAccent)

        add_loan_balance_balance.addTextChangedListener(AddLoanBalanceEditTextWatcher(add_loan_balance_balance))
        add_loan_balance_base_payment.addTextChangedListener(AddLoanBalanceEditTextWatcher(add_loan_balance_base_payment))
        add_loan_balance_extra_payment.addTextChangedListener(AddLoanBalanceEditTextWatcher(add_loan_balance_extra_payment))
    }

    override fun commitToLoan(loan: Loan) {
        loan.balance = add_loan_balance_balance.convertToDouble()
        loan.payment = add_loan_balance_base_payment.convertToDouble()
        loan.extraPayment = add_loan_balance_extra_payment.convertToDouble()
    }

    override fun animateTheViewsIn() {
        if (firstLoad) {
            add_loan_balance_title?.visibility = View.INVISIBLE
            add_loan_balance_container?.visibility = View.INVISIBLE
            add_loan_balance_base_payment_container?.visibility = View.INVISIBLE
            add_loan_balance_extra_payment_container?.visibility = View.INVISIBLE

            val animTime = context?.resources?.getInteger(android.R.integer.config_mediumAnimTime)?.toLong() ?: 300L

            add_loan_balance_title?.waitForLayoutToFinish {
                animateInFromLeft(AnimationBuilder.Builder
                        .postAnimation(animTime)
                        .postAnimFunction {
                            add_loan_balance_container?.animateInFromBottom(AnimationBuilder
                                    .postAnimFunction {
                                        add_loan_balance_base_payment_container?.animateInFromBottom(defaultAnimBuilder)
                                        add_loan_balance_extra_payment_container?.animateInFromBottom(defaultAnimBuilder)
                                    }
                                    .build())
                        }
                        .build())
            }
        }
    }
}