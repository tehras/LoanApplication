package com.github.tehras.loanapplication.ui.addloan.fragments.intro

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.extensions.populateDrawable
import com.github.tehras.loanapplication.ui.addloan.AddLoanActivity
import kotlinx.android.synthetic.main.fragment_add_loan_intro.*

class AddLoanIntroFragment : Fragment() {

    companion object {
        fun instance(): AddLoanIntroFragment = AddLoanIntroFragment()
    }

    val listener: () -> Unit = { lets_get_started_intro.populateDrawable(R.drawable.graduation) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_add_loan_intro, container, false)

        v.findViewById(R.id.lets_get_started_intro).postDelayed(listener, 17)

        return v
    }

    override fun onStart() {
        super.onStart()

        (activity as AddLoanActivity).hideButtons()
        start_button.setOnClickListener { (activity as AddLoanActivity).startAddLoanFlow() }
    }
}