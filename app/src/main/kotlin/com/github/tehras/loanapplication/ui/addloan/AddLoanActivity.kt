package com.github.tehras.loanapplication.ui.addloan

import android.os.Bundle
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.extensions.*
import com.github.tehras.loanapplication.ui.addloan.fragments.basic.AddLoanBasicFragment
import com.github.tehras.loanapplication.ui.base.PresenterActivity
import kotlinx.android.synthetic.main.activity_add_loan.*
import timber.log.Timber


/**
 * Created by tehras on 11/10/16.
 *
 * This activity is to add a loan
 */
class AddLoanActivity : PresenterActivity<AddLoanView, AddLoanPresenter>(), AddLoanView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_loan)

        //initialize before and after buttons
        initPrevAndNextButtons()

        if (savedInstanceState == null) {
            //start first fragment
            AddLoanBasicFragment.instance().startFragment(R.id.add_loan_fragment_container, activity = this)
            this.enterCircularReveal() //animate
        } else {
            loan = savedInstanceState.getParcelable<Loan>(ARG_LOAN_OBJECT)
            addLoanStage = AddLoanStage.converToStage(savedInstanceState.getInt(ARG_LOAN_STAGE))
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        Timber.d("onSaveInstance")
        outState?.putInt(ARG_LOAN_STAGE, addLoanStage.stage)
        outState?.putParcelable(ARG_LOAN_OBJECT, loan)
    }

    private val ARG_LOAN_STAGE = "bundle_loan_stage"
    private val ARG_LOAN_OBJECT = "bundle_loan"

    private var addLoanStage: AddLoanStage = AddLoanStage.BASIC_INFORMATION
    private var loan: Loan = Loan()

    override fun onBackPressed() {
        if (!supportFragmentManager.popBackStackImmediate()) {
            //tood animate then close
            exitCircularReveal(add_loan_prev_button.centerX(), add_loan_prev_button.centerY())
        }
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(0, 0)
    }

    private fun initPrevAndNextButtons() {
        add_loan_prev_button.setOnClickListener {
            onBackPressed()
        }

        add_loan_next_button.setOnClickListener {
            //go to next fragment
            onNextPressed()
        }
    }

    private fun onNextPressed() {
        //switch case
        when (addLoanStage) {
            AddLoanStage.BASIC_INFORMATION -> {
                //start BASIC_INFORMATION_FRAGMENT
            }
        }
    }

    override fun loanAddedSuccessfully() {

    }

    override fun loanCouldNotBeAdded() {

    }

    override fun startLoading() {

    }

    override fun stopLoading() {

    }

    override fun injectDependencies(graph: AppComponent) {
        graph.plus(AddLoanModule(this))
                .injectTo(this)
    }

}