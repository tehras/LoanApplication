package com.github.tehras.loanapplication.ui.addloan.fragments.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.extensions.*
import com.github.tehras.loanapplication.ui.addloan.AddLoanActivity
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanFragmentModule
import com.github.tehras.loanapplication.ui.base.PresenterFragment
import kotlinx.android.synthetic.main.fragment_add_loan_review.*

class AddLoanReviewFragment : PresenterFragment<AddLoanReviewView, AddLoanReviewPresenter>(), AddLoanReviewView {

    companion object {
        private val ARG_LOAN = "ARG_LOAN"

        fun instance(loan: Loan): AddLoanReviewFragment {
            return AddLoanReviewFragment().addToBundle { putParcelable(ARG_LOAN, loan) }
        }
    }

    override fun injectDependencies(graph: AppComponent) {
        graph.plus(AddLoanFragmentModule(this)).injectTo(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_loan_review, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (firstLoad) {
            animateAllElements()
        }

        activity?.let {
            if (activity is AddLoanActivity)
                (activity as AddLoanActivity).showSubmitButtons()
        }

        attachListeners()
    }

    override fun onStop() {
        super.onStop()

        activity?.let {
            if (activity is AddLoanActivity)
                (activity as AddLoanActivity).showButtons()
        }
    }

    /**
     * Attaches listeners
     */
    private fun attachListeners() {

    }

    /**
     * Will animate all the contianers
     */
    private fun animateAllElements() {
        val animTime = context?.resources?.getInteger(android.R.integer.config_mediumAnimTime)?.toLong() ?: 300L

        add_loan_review.visibility = View.INVISIBLE
        add_loan_review_balance_info_container.visibility = View.INVISIBLE
        add_loan_review_basic_info_container.visibility = View.INVISIBLE
        add_loan_review_other_info_container.visibility = View.INVISIBLE

        add_loan_review?.waitForLayoutToFinish {
            animateInFromLeft(AnimationBuilder.Builder
                    .postAnimation(animTime)
                    .postAnimFunction {
                        add_loan_review_basic_info_container?.animateInFromLeft(defaultAnimBuilder)
                        add_loan_review_balance_info_container?.animateInFromRight(defaultAnimBuilder)
                        add_loan_review_other_info_container?.animateInFromBottom(defaultAnimBuilder)
                    }
                    .build())
        }
    }

}