package com.github.tehras.loanapplication.ui.login

import android.content.Intent
import android.view.View
import com.github.tehras.loanapplication.ui.base.Presenter

interface LoginPresenter : Presenter<LoginView>, View.OnClickListener {
    fun  onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}