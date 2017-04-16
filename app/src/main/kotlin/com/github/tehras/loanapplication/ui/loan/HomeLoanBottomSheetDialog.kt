package com.github.tehras.loanapplication.ui.loan

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.Payment
import com.github.tehras.loanapplication.data.remote.models.SinglePaymentResponse
import com.github.tehras.loanapplication.extensions.*
import com.github.tehras.loanapplication.ui.base.PresenterBottomSheetFragment
import com.github.tehras.loanapplication.ui.home.HomeLoanActivity
import kotlinx.android.synthetic.main.bottom_sheet_loan_layout.*
import kotlinx.android.synthetic.main.home_loan_chart_layout.*
import kotlinx.android.synthetic.main.loading_view.*
import timber.log.Timber
import java.util.*

/**
 * Home Loan Sheet Dialog
 */
open class HomeLoanBottomSheetDialog : PresenterBottomSheetFragment<HomeLoanSingleView, HomeLoanSinglePresenter>(), HomeLoanSingleView {
    override fun startDeleteLoading() {
        loading_view.show()
    }

    override fun stopDeleteLoading() {
        loading_view.hide()
    }

    override fun successDeletingLoan() {
        //close the dialog then show Snackbar
        Snackbar.make(activity.getRootView(), "Loan was deleted successfully", Snackbar.LENGTH_LONG)
                .show()

        if (activity is HomeLoanActivity) {
            (activity as HomeLoanActivity).refreshData()
        }

        this.dialog.dismiss()
    }

    override fun errorDeletingLoan() {
        Snackbar.make(activity.getRootView(), "Loan could not be deleted at this time", Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    presenter.deleteLoan(loan)
                }.show()
    }

    override fun injectDependencies(graph: AppComponent) {
        graph.plus(HomeLoanSingleModule(this))
                .injectTo(this)
    }

    override fun updateChart(payments: ArrayList<SinglePaymentResponse>) {
        @Suppress("DEPRECATION")
        loan_sheet_chart.setChartColor(context.resources.getColor(R.color.colorYellow))
                .updateData(firstPayment(payments), true)
        loan_sheet_chart.visibility = View.VISIBLE
    }

    private fun firstPayment(payments: ArrayList<SinglePaymentResponse>): ArrayList<Payment>? {
        if (payments.size > 0)
            return payments[0].payments

        return ArrayList()
    }

    override fun errorNoNetwork() {
        loan_sheet_error_layout.visibility = View.VISIBLE
    }

    override fun errorFetchData() {
        loan_sheet_error_layout.visibility = View.VISIBLE
    }

    override fun startChartLoading() {
        loan_sheet_error_layout.visibility = View.GONE
        loading_layout.visibility = View.VISIBLE

        val drawable = loading_icon.drawable
        if (drawable is Animatable) {
            Timber.d("start animating")
            drawable.start()
        }
    }

    override fun stopChartLoading() {
        loading_layout.visibility = View.GONE
    }

    companion object {
        private val ARG_LOAN = "argument_loan_key"

        fun getInstance(loan: Loan): HomeLoanBottomSheetDialog {
            return HomeLoanBottomSheetDialog().addToBundle { putParcelable(ARG_LOAN, loan) }
        }
    }

    private var loan: Loan? = null

    override fun onPresenterAvailable() {
        super.onPresenterAvailable()

        this.loan = arguments.getParcelable<Loan>(ARG_LOAN)

        initChartView()

        loan_sheet_balance.text = loan?.balance?.dollarWithTwoDecimalsFormat() ?: ""
        loan_sheet_base_payment.text = loan?.payment?.dollarWithTwoDecimalsFormat() ?: ""
        loan_sheet_extra_payment.text = loan?.extraPayment?.dollarWithTwoDecimalsFormat() ?: ""
        loan_sheet_provider.text = loan?.provider
        loan_sheet_name.text = loan?.name
        loan_sheet_interest.text = loan?.interest?.percentageFormat() ?: ""
        loan_sheet_start_date.text = loan?.repaymentStartDate?.formatDate("yyyyMMdd", "MMM dd, yyyy")

        loan?.let { presenter.getSingleRepayments(it) }

        loan_delete.setOnClickListener { deleteLoan() }
        loan_edit.setOnClickListener { editLoan() }
    }

    private fun editLoan() {
        //todo
        Snackbar.make(loan_edit.rootView, "Coming Soon", Snackbar.LENGTH_SHORT).show()
    }

    private fun deleteLoan() {
        AlertDialog.Builder(context).setMessage("Are you sure you want to delete ${loan?.name ?: "this"}?")
                .setPositiveButton("Delete", { _, _ -> presenter.deleteLoan(loan) })
                .setNegativeButton("No", { p1, p2 -> p1.dismiss() })
                .show().setButtonColors(R.color.colorRed, R.color.colorPrimary)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_loan_layout, container)
    }

    private fun initChartView() {
        loan_sheet_error_layout.visibility = View.GONE
        loan_sheet_chart.visibility = View.INVISIBLE
        loading_layout.visibility = View.GONE
        @Suppress("DEPRECATION")
        line_chart_layout_background.setBackgroundColor(context.resources.getColor(android.R.color.transparent))
        loan_error_refresh.setOnClickListener {
            presenter.getSingleRepayments(loan)
        }
    }
}