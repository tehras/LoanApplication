package com.github.tehras.loanapplication.ui.addloan.fragments.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.ui.base.PresenterFragment

class AddLoanBasicFragment : PresenterFragment<AddLoanBasicView, AddLoanBasicPresenter>(), AddLoanBasicView {

    companion object {
        fun instance(): AddLoanBasicFragment {
            return AddLoanBasicFragment()
        }
    }

    override fun injectDependencies(graph: AppComponent) {
        graph.plus(AddLoanBasicModule(this))
                .injectTo(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_add_loan_basic, container, false)


        return contentView
    }

}