package com.github.tehras.loanapplication.ui.addloan.fragments.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.extensions.*
import com.github.tehras.loanapplication.ui.base.PresenterFragment
import kotlinx.android.synthetic.main.fragment_add_loan_basic.*

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

    override fun onStart() {
        super.onStart()

        //animate the textViews once visible
        animateTheViewsIn()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_loan_basic, container, false)
    }

    private fun animateTheViewsIn() {
        add_loan_loan_name_container.visibility = View.INVISIBLE
        add_loan_basic_title.waitForLayoutToFinish {
            animateInFromLeft(AnimationBuilder.Builder
                    .postAnimation(this.getInteger(android.R.integer.config_mediumAnimTime).toLong())
                    .postAnimFunction {
                        add_loan_loan_name_container.animateInFromBottom(defaultAnimBuilder)
                    }
                    .build())
        }
    }

}