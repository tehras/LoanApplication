package com.github.tehras.loanapplication.ui.addloan

import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.extensions.EXTRA_RADIUS_COORDINATE
import com.github.tehras.loanapplication.extensions.EXTRA_X_COORDINATE
import com.github.tehras.loanapplication.extensions.EXTRA_Y_COORDINATE
import com.github.tehras.loanapplication.extensions.startFragment
import com.github.tehras.loanapplication.ui.addloan.fragments.basic.AddLoanBasicFragment
import com.github.tehras.loanapplication.ui.base.PresenterActivity
import kotlinx.android.synthetic.main.activity_add_loan.*


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
            root_view.visibility = View.INVISIBLE

            val viewTreeObserver = root_view.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        circularRevealActivity()
                        root_view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        } else {
            loan = savedInstanceState.getParcelable<Loan>(ARG_LOAN_OBJECT)
            addLoanStage = AddLoanStage.converToStage(savedInstanceState.getInt(ARG_LOAN_STAGE))
        }
    }

    private fun circularRevealActivity() {
        val extras = intent.extras


        var cx = root_view.width / 2
        var cy = root_view.height / 2
        var radius = 0

        if (extras != null) {
            cx = intent.getIntExtra(EXTRA_X_COORDINATE, cx)
            cy = intent.getIntExtra(EXTRA_Y_COORDINATE, cy)
            radius = intent.getIntExtra(EXTRA_RADIUS_COORDINATE, radius)
        }
        val finalRadius = Math.max(root_view.width, root_view.height).toFloat()

        // create the animator for this view (the start radius is zero)
        val circularReveal = ViewAnimationUtils.createCircularReveal(root_view, cx, cy, radius.toFloat(), finalRadius)
        circularReveal.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

        // make the view visible and start the animation
        root_view.visibility = View.VISIBLE
        circularReveal.start()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt(ARG_LOAN_STAGE, addLoanStage.stage)
        outState?.putParcelable(ARG_LOAN_OBJECT, loan)
    }

    private val ARG_LOAN_STAGE = "bundle_loan_stage"
    private val ARG_LOAN_OBJECT = "bundle_loan"

    private var addLoanStage: AddLoanStage = AddLoanStage.BASIC_INFORMATION
    private var loan: Loan = Loan()

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