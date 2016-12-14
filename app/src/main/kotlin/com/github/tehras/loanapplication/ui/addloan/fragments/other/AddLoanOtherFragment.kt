package com.github.tehras.loanapplication.ui.addloan.fragments.other

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.extensions.*
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseComponent
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseFragment
import kotlinx.android.synthetic.main.fragment_add_loan_other.*
import java.text.SimpleDateFormat
import java.util.*

class AddLoanOtherFragment : AddLoanBaseFragment<AddLoanOtherView, AddLoanOtherPresenter>(), AddLoanOtherView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_loan_other, container, false)
    }

    companion object {
        fun instance(): AddLoanOtherFragment {
            return AddLoanOtherFragment()
        }
    }

    override fun onStart() {
        super.onStart()

        view?.let {
            add_loan_other_repayment_help.setOnClickListener { Snackbar.make(it, "Coming Soon", Snackbar.LENGTH_SHORT).show() }
        }

        add_loan_other_interest.addTextChangedListener(AddLoanPercentageEditTextWatcher(add_loan_other_interest))
        add_loan_other_repayment.addTextChangedListener(AddLoanDateEditTextWatcher(add_loan_other_repayment))

        add_loan_other_interest.filterBackground(R.color.colorAccent)
        add_loan_other_repayment.filterBackground(R.color.colorAccent)
    }

    override fun animateTheViewsIn() {
        if (firstLoad) {
            add_loan_other_interst_container.visibility = View.INVISIBLE
            add_loan_other_repayment_container.visibility = View.INVISIBLE

            val animTime = context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
            add_loan_other_title?.waitForLayoutToFinish {
                animateInFromLeft(AnimationBuilder.Builder
                        .postAnimation(animTime)
                        .postAnimFunction {
                            add_loan_other_interst_container?.animateInFromBottom(AnimationBuilder
                                    .postAnimFunction { add_loan_other_repayment_container?.animateInFromBottom(defaultAnimBuilder) }
                                    .build())
                        }
                        .build())
            }
        }

    }

    override fun injectTo(plus: AddLoanBaseComponent) {
        plus.injectTo(this)
    }

    override fun validateAnswers(): Boolean {
        return isValidInterest() && isValidDate()
    }

    private fun isValidInterest(): Boolean {
        return add_loan_other_interest.isValidPercentage(0.00, 30.00)
    }

    private fun isValidDate(): Boolean {
        //has to be before today and after 1901
        val text = add_loan_other_repayment.text.toString()
        var error = ""
        try {
            val date = SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(text)

            val cal = Calendar.getInstance()
            cal.time = date

            val today = Calendar.getInstance()

            val year = cal.get(Calendar.YEAR)

            if (year > 10 + today.get(Calendar.YEAR)) {
                error = "Year must be before ${today.get(Calendar.YEAR) + 10}"
            } else if (year < today.get(Calendar.YEAR) - 50) {
                error = "Year must be after ${today.get(Calendar.YEAR) - 50}"
            }

        } catch (e: Exception) {
            error = "Date format must be mm/dd/yyyy"
        }

        if (error.isEmpty())
            return true
        else
            add_loan_other_repayment.error = error

        return false
    }

    override fun commitToLoan(loan: Loan) {
        loan.interest = add_loan_other_interest.convertToDouble()
        loan.repaymentStartDate = add_loan_other_repayment.convertToDate()
    }

}
