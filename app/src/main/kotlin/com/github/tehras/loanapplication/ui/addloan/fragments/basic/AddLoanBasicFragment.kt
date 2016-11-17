package com.github.tehras.loanapplication.ui.addloan.fragments.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.extensions.*
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseComponent
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseFragment
import kotlinx.android.synthetic.main.fragment_add_loan_basic.*

class AddLoanBasicFragment : AddLoanBaseFragment<AddLoanBasicView, AddLoanBasicPresenter>(), AddLoanBasicView {
    override fun injectTo(plus: AddLoanBaseComponent) {
        plus.injectTo(this)
    }

    override fun validateAnswers(): Boolean {
        val isValidLoanName = isValidLoanName(add_loan_basic_loan_name)

        return isValidLoanName
    }

    private fun isValidLoanName(tv: TextView): Boolean {
        return tv.isValidLength(3, 15)
    }

    companion object {
        fun instance(): AddLoanBasicFragment {
            return AddLoanBasicFragment()
        }
    }

    override fun onStart() {
        super.onStart()

        //animate the textViews once visible
        animateTheViewsIn()

        //add text watcher
        add_loan_basic_loan_name.addErrorTextWatcher { isValidLoanName(tv = this) }
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