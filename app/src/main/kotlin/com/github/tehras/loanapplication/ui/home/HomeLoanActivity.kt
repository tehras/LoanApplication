package com.github.tehras.loanapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.PaymentsResponse
import com.github.tehras.loanapplication.extensions.*
import com.github.tehras.loanapplication.ui.addloan.AddLoanActivity
import com.github.tehras.loanapplication.ui.base.PresenterActivity
import kotlinx.android.synthetic.main.activity_loan.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.home_app_bar_layout.*
import kotlinx.android.synthetic.main.loading_view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class HomeLoanActivity : PresenterActivity<HomeLoanView, HomeLoanPresenter>(), HomeLoanView {
    var payments: PaymentsResponse? = null

    override fun updateChart(payments: PaymentsResponse) {
        Timber.d("chartUpdated - ${payments.payments.size}")
        this.payments = payments
        home_payment_chart_layout.updateData(payments.payments)
    }

    override fun updateList(loans: ArrayList<Loan>) {
        adapter.updateLoans(loans)
        home_loan_total_balance.text = getTotalBalance(loans)
        updateEmptyView(false, "No loans found")
    }

    private fun getTotalBalance(loans: ArrayList<Loan>): CharSequence? {
        var totalBalance: Double = 0.toDouble()
        loans.forEach {
            totalBalance += it.balance
        }

        return totalBalance.dollarWithTwoDecimalsFormat()
    }

    override fun startLoading() {
        loading_view.show()
        empty_view.hide()
    }

    override fun stopLoading() {
        loading_view.hide()
    }

    override fun errorNoNetwork() {
        stopLoading()
        updateEmptyView(true, "Network error, please try again")
    }

    override fun errorFetchData() {
        stopLoading()
        updateEmptyView(true, "Error retrieving data, please try again")
    }

    lateinit var component: HomeLoanComponent

    override fun injectDependencies(graph: AppComponent) {
        this.component = graph.plus(HomeLoanModule(this))
        this.component.injectTo(this)
    }


    @Inject
    lateinit var layoutManager: LinearLayoutManager
    @Inject
    lateinit var adapter: HomeLoanListAdapter
    private var firstLoad: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)
        setSupportActionBar(toolbar)

        setupRecyclerView()
        setupFab()
        appbar_layout.setupElevationListener(toolbar)
    }

    private fun setupFab() {
        home_add_button.setOnClickListener {
            val intent = Intent(this, AddLoanActivity::class.java)
            intent.putExtra(EXTRA_X_COORDINATE, (home_add_button.left + home_add_button.right) / 2)
            intent.putExtra(EXTRA_Y_COORDINATE, (home_add_button.top + home_add_button.bottom) / 2)
            intent.putExtra(EXTRA_RADIUS_COORDINATE, home_add_button.right - home_add_button.left)

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        refreshData()
    }

    private fun refreshData() {
        presenter.getLoans(firstLoad)
        firstLoad = false
    }

    private fun setupRecyclerView() {
        listRecyclerView.adapter = adapter
        listRecyclerView.layoutManager = layoutManager

        adapter.setClickListener {
            onItemClick(it)
        }
    }

    private fun onItemClick(loan: Loan) {
        presenter.showLoanBottomSheet(loan, this)
    }


    private fun updateEmptyView(b: Boolean, error: String) {
        if (adapter.itemCount == 0) {
            Timber.d("showing empty view")
            empty_view.show()
            empty_view_text.text = error
            if (b) {
                empty_view_retry_button.visibility = View.VISIBLE
                empty_view_retry_button.setOnClickListener { presenter.getLoans(true) }
            } else {
                empty_view_retry_button.visibility = View.GONE
            }
        } else {
            Timber.d("hiding empty view")
            empty_view.hide()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(ARG_LOANS_KEY, adapter.getLoans())
        outState.putParcelable(ARG_PAYMENTS_KEY, payments)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val loans: ArrayList<Loan> = savedInstanceState.getParcelableArrayList(ARG_LOANS_KEY)
        val payments: PaymentsResponse? = savedInstanceState.getSafeParcelable(ARG_PAYMENTS_KEY)

        if (payments != null) {
            updateList(loans)
            updateChart(payments)
        } else {
            refreshData()
        }
    }

    companion object {
        val ARG_LOANS_KEY = "arg_loans_key"
        val ARG_PAYMENTS_KEY = "arg_payments_key"
    }

}
