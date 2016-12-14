package com.github.tehras.loanapplication.ui.login

import com.github.tehras.loanapplication.ui.base.MvpView
import com.google.firebase.auth.FirebaseAuth

interface LoginView : MvpView {
    fun sendBackFailedUse(s: String)
    fun sendBackSuccessUser(auth: FirebaseAuth)
    fun showLoading()
    fun stopLoading()
    fun showAuthenticationFailed()
}
