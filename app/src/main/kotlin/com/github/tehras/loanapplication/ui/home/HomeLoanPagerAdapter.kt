package com.github.tehras.loanapplication.ui.home

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan


class HomeLoanPagerAdapter(var loans: ArrayList<Loan>?) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val layout = inflater.inflate(R.layout.home_loan_card, container, false) as ViewGroup
        container.addView(layout)

        return layout
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