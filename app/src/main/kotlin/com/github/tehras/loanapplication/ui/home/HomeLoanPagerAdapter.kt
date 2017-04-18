package com.github.tehras.loanapplication.ui.home

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.extensions.dollarWithTwoDecimalsFormat
import timber.log.Timber


class HomeLoanPagerAdapter(var loans: ArrayList<Loan>?) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Timber.d("instantiate item")

        val inflater = LayoutInflater.from(container.context)
        val layout = inflater.inflate(R.layout.home_loan_card, container, false) as ViewGroup

        populateLayout(layout, loans?.get(position))

        container.addView(layout)

        return layout
    }

    private fun populateLayout(view: View, loan: Loan?) {
        loan?.let {
            val provider: TextView = view.findViewById(R.id.home_loan_provider) as TextView
            val name: TextView = view.findViewById(R.id.home_loan_name) as TextView
            val amount: TextView = view.findViewById(R.id.home_loan_amount) as TextView

            provider.text = loan.provider
            name.text = loan.name
            amount.text = loan.balance.dollarWithTwoDecimalsFormat()
        }
    }

    fun scrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any?) {
        container.removeView(view as View?)
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return loans?.size ?: 0
    }

}