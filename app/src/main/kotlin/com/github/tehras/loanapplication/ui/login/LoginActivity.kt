package com.github.tehras.loanapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.extensions.hide
import com.github.tehras.loanapplication.extensions.show
import com.github.tehras.loanapplication.extensions.startLoggedInActivity
import com.github.tehras.loanapplication.ui.base.PresenterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_activity.*
import kotlinx.android.synthetic.main.loading_view.*
import timber.log.Timber

class LoginActivity : PresenterActivity<LoginView, LoginPresenter>(), LoginView {
    override fun sendBackFailedUse(s: String) {
        Timber.d("sendBackFailedUse $s")
        Snackbar.make(login_google_button, s, Snackbar.LENGTH_LONG).show()
    }

    override fun sendBackSuccessUser(auth: FirebaseAuth) {
        Timber.d("sendBackSuccessUser")

        auth.currentUser?.let {
            this.startLoggedInActivity(it, { stopLoading() }, { sendBackFailedUse("Error Logging In, Please Try Again Later") })
        }
    }

    override fun showLoading() {
        loading_view.show()
    }

    override fun stopLoading() {
        loading_view.hide()
    }

    override fun showAuthenticationFailed() {
        Snackbar.make(login_google_button, "Authentication failed", Snackbar.LENGTH_LONG).show()
    }

    override fun injectDependencies(graph: AppComponent) {
        graph.plus(LoginModule(this)).injectTo(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login_activity)
    }

    override fun onStart() {
        super.onStart()

        login_google_button.setOnClickListener(presenter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        presenter.onActivityResult(requestCode, resultCode, data)
    }

}