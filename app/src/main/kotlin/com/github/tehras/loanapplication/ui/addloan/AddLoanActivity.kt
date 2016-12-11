package com.github.tehras.loanapplication.ui.addloan

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.View
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.extensions.*
import com.github.tehras.loanapplication.ui.addloan.fragments.AddLoanBaseFragment
import com.github.tehras.loanapplication.ui.addloan.fragments.balance.AddLoanBalanceFragment
import com.github.tehras.loanapplication.ui.addloan.fragments.basic.AddLoanBasicFragment
import com.github.tehras.loanapplication.ui.addloan.fragments.intro.AddLoanIntroFragment
import com.github.tehras.loanapplication.ui.addloan.fragments.other.AddLoanOtherFragment
import com.github.tehras.loanapplication.ui.addloan.fragments.review.AddLoanReviewFragment
import com.github.tehras.loanapplication.ui.base.PresenterActivity
import kotlinx.android.synthetic.main.activity_add_loan.*
import kotlinx.android.synthetic.main.loading_view.*
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
            AddLoanIntroFragment.instance().startFragment(R.id.add_loan_fragment_container, activity = this)
            this.enterCircularReveal() //animate
        } else {
            loan = savedInstanceState.getParcelable<Loan>(ARG_LOAN_OBJECT)
            addLoanStage = AddLoanStage.convertToStage(savedInstanceState.getInt(ARG_LOAN_STAGE))
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
    var loan: Loan = Loan()

    override fun onBackPressed() {
        if (addLoanStage == AddLoanStage.PREVIEW_INFORMATION) {
            //show alert
            exitAlert("Are you sure you'd like to go back? You'll lose this loan forever.")
        } else if (!supportFragmentManager.popBackStackImmediate()) {
            if (this.getTopFragment() is AddLoanIntroFragment)
                exitCircularReveal(add_loan_next_button.centerX(), add_loan_next_button.centerY())
            else
                exitCircularReveal(add_loan_prev_button.centerX(), add_loan_prev_button.centerY())
        }
    }

    private fun exitAlert(message: String) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("YES") { dialog, p1 ->
                    dialog.dismiss()
                    this@AddLoanActivity.finish()
                }.setNegativeButton("CANCEL") { dialog, p1 -> dialog.dismiss() }.create().show()
    }

    fun startAddLoanFlow() {
        showButtons()
        AddLoanBasicFragment.instance().startFragment(R.id.add_loan_fragment_container, this)
        setImage(addLoanStage.bottomGrad)
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

        add_loan_exit.setOnClickListener {
            exitAlert("Are you sure you'd like to exit and discard the data?")
        }
    }

    private fun onNextPressed() {
        if (addLoanStage == AddLoanStage.PREVIEW_INFORMATION) {
            Timber.d("insideLoanStage")
            //submit
            presenter.createLoan(loan)
        } else if (notifyFragmentThatNextWasPressed()) {
            //switch case
            addLoanStage = AddLoanStage.convertToStage(addLoanStage.stage + 1) //add one more
            when (addLoanStage) {
                AddLoanStage.BASIC_INFORMATION -> AddLoanBasicFragment.instance().startFragment(R.id.add_loan_fragment_container, this, true)
                AddLoanStage.BALANCE_INFORMATION -> AddLoanBalanceFragment.instance().startFragment(R.id.add_loan_fragment_container, this, true)
                AddLoanStage.OTHER_INFORMATION -> AddLoanOtherFragment.instance().startFragment(R.id.add_loan_fragment_container, this, true)
                AddLoanStage.PREVIEW_INFORMATION -> AddLoanReviewFragment.instance(loan).startFragment(R.id.add_loan_fragment_container, this, true)
            }
            setImage(addLoanStage.bottomGrad)
        }
    }

    private fun setImage(bottomGrad: Int) {
        if (bottomGrad == -1)
            add_loan_bottom_image.visibility = View.GONE
        else {
            add_loan_bottom_image.visibility = View.VISIBLE
            add_loan_bottom_image.populateDrawable(bottomGrad)
        }
    }

    private fun notifyFragmentThatNextWasPressed(): Boolean {
        supportFragmentManager.let {
            val lastFragment = supportFragmentManager.findFragmentById(R.id.add_loan_fragment_container) //get last fragment
            if (lastFragment is AddLoanBaseFragment<*, *>) {
                val isValid = lastFragment.validateAnswers()
                if (isValid)
                    lastFragment.commitToLoan(loan)

                return isValid
            }
        }

        return false
    }

    override fun loanAddedSuccessfully() {
        Snackbar.make(root_view, "Loan added successfully", Snackbar.LENGTH_SHORT).show()

        this.finish()
    }

    override fun loanCouldNotBeAdded() {
        Snackbar.make(root_view, "Loan could not be created", Snackbar.LENGTH_SHORT).show()
    }

    override fun startLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun stopLoading() {
        loading_view.visibility = View.GONE
    }

    override fun injectDependencies(graph: AppComponent) {
        graph.plus(AddLoanModule(this))
                .injectTo(this)
    }

    fun hideButtons() {
        add_loan_prev_button.visibility = View.INVISIBLE
        add_loan_next_button.visibility = View.INVISIBLE
        add_loan_exit.visibility = View.INVISIBLE
    }

    fun showButtons() {
        add_loan_prev_button.show()
        add_loan_next_button.show()
        add_loan_exit.visibility = View.VISIBLE
    }

    @Suppress("DEPRECATION")
    fun showSubmitButtons() {
        add_loan_prev_button.setImageDrawable(resources.getDrawable(R.drawable.ic_exit_white))
        add_loan_next_button.setImageDrawable(resources.getDrawable(R.drawable.ic_check_mark))

        add_loan_exit.visibility = View.INVISIBLE
    }

}