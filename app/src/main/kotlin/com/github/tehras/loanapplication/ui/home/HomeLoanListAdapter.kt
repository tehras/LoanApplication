package com.github.tehras.loanapplication.ui.home

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.extensions.dollarWithTwoDecimalsFormat
import com.github.tehras.loanapplication.extensions.inflateLayout
import com.github.tehras.loanapplication.ui.ActivityScope
import kotlinx.android.synthetic.main.home_loan_item.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by tehras on 11/5/16.
 *
 * Displays list of loans
 */
@ActivityScope
class HomeLoanListAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var loans: List<Loan>
    private var itemClick: ((Loan) -> Unit)?

    init {
        loans = emptyList<Loan>()
        itemClick = null
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.context.inflateLayout(R.layout.home_loan_item, parent, false)
        return LoanViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is LoanViewHolder) {
            val loan = loans[position]
            holder.bindLoan(loan)
        }
    }

    override fun getItemCount(): Int = loans.size

    fun updateLoans(loans: ArrayList<Loan>) {
        val prevLoans = this.loans
        if (prevLoans.isNotEmpty()) {
            //find what is the difference
            val prevSize = prevLoans.size
            if (prevSize == loans.size) {
                this.loans = loans
                notifyItemRangeChanged(0, itemCount)
            } else {
                //todo find te missing piece
                this.loans = loans
                notifyDataSetChanged()
            }
        } else {
            this.loans = loans
            notifyItemRangeInserted(0, itemCount)
        }
    }

    fun setClickListener(itemClick: ((Loan) -> Unit)?) {
        this.itemClick = itemClick
    }

    fun getLoans() = ArrayList<Loan>(loans)

    class LoanViewHolder(itemView: View,
                         val itemClick: ((Loan) -> Unit)?) : RecyclerView.ViewHolder(itemView) {

        fun bindLoan(loan: Loan) = with(itemView) {
            itemView.setOnClickListener {
                itemClick?.invoke(loan)
            }

            home_loan_amount.text = loan.balance.dollarWithTwoDecimalsFormat()
            home_loan_name.text = loan.name
            home_loan_provider.text = loan.provider
        }

    }

}