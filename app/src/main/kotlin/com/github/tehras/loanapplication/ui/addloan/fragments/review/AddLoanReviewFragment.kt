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
import com.github.tehras.loanapplication.ui.addloan.AddLoanStage
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

    private var loan: Loan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            loan = it.getParcelable(ARG_LOAN)
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

        populateLayouts()
        attachListeners()
    }

    private fun populateLayouts() {
        add_loan_basic_loan_name.text = loan?.name
        add_loan_basic_loan_provider.text = loan?.provider
        add_loan_balance_balance.text = loan?.balance?.dollarWithTwoDecimalsFormat()
        add_loan_balance_base_payment.text = loan?.payment?.dollarWithTwoDecimalsFormat()
        add_loan_balance_extra_payment.text = loan?.extraPayment?.dollarWithTwoDecimalsFormat()
        add_loan_other_interest.text = loan?.interest?.percentageFormat()
        add_loan_other_repayment.text = loan?.repaymentStartDate?.convertToDate("MM/dd/yyyy", "yyyyMMdd")

        add_loan_balance_basic_edit.setOnClickListener { startReviewFragment(AddLoanStage.BASIC_INFORMATION) }
        add_loan_balance_edit.setOnClickListener { startReviewFragment(AddLoanStage.BALANCE_INFORMATION) }
        add_loan_other_edit.setOnClickListener { startReviewFragment(AddLoanStage.OTHER_INFORMATION) }
    }

    private fun startReviewFragment(stage: AddLoanStage) {
        if (activity != null && activity is AddLoanActivity) {
            (activity as AddLoanActivity).startReviewFragment(stage)
        }
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
        //we can leave it blank for this fragment
    }

    /**
     * Will animate all the containers
     */
    private fun animateAllElements() {
        if (firstLoad) {
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

}