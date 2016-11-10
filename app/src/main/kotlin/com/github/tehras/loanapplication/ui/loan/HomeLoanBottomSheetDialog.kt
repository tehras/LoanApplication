package com.github.tehras.loanapplication.ui.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.SinglePaymentResponse
import com.github.tehras.loanapplication.extensions.addToBundle
import com.github.tehras.loanapplication.extensions.dollarWithTwoDecimalsFormat
import com.github.tehras.loanapplication.extensions.formatDate
import com.github.tehras.loanapplication.extensions.percentageFormat
import com.github.tehras.loanapplication.ui.base.PresenterBottomSheetFragment
import com.github.tehras.loanapplication.ui.home.HomeLoanActivity
import kotlinx.android.synthetic.main.bottom_sheet_loan_layout.*
import java.util.*

/**
 * Home Loan Sheet Dialog
 */
open class HomeLoanBottomSheetDialog : PresenterBottomSheetFragment<HomeLoanSingleView, HomeLoanSinglePresenter>(), HomeLoanSingleView {

    override fun injectDependencies(graph: AppComponent) {
        graph.plus(HomeLoanSingleModule(this))
                .injectTo(this)
//        (this.activity as HomeLoanActivity).component.injectTo(this)
    }

    override fun updateChart(payments: ArrayList<SinglePaymentResponse>) {

    }

    override fun errorNoNetwork() {

    }

    override fun errorFetchData() {

    }

    override fun startChartLoading() {

    }

    override fun stopChartLoading() {

    }

    companion object {
        private val ARG_LOAN = "argument_loan_key"

        fun getInstance(loan: Loan): HomeLoanBottomSheetDialog {
            return HomeLoanBottomSheetDialog().addToBundle { putParcelable(ARG_LOAN, loan) }
        }
    }

    private var loan: Loan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.loan = arguments.getParcelable<Loan>(ARG_LOAN)
    }

    override fun onStart() {
        super.onStart()

        loan_sheet_balance.text = loan?.balance?.dollarWithTwoDecimalsFormat() ?: ""
        loan_sheet_base_payment.text = loan?.payment?.dollarWithTwoDecimalsFormat() ?: ""
        loan_sheet_extra_payment.text = loan?.extraPayment?.dollarWithTwoDecimalsFormat() ?: ""
        loan_sheet_provider.text = loan?.provider
        loan_sheet_name.text = loan?.name
        loan_sheet_interest.text = loan?.interest?.percentageFormat() ?: ""
        loan_sheet_start_date.text = loan?.repaymentStartDate?.formatDate("yyyyMMdd", "MMM dd, yyyy")

        loan?.let { presenter.getSingleRepayments(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_loan_layout, container)
    }
}