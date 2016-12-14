package com.github.tehras.loanapplication.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.tehras.loanapplication.extensions.startLoggedInActivity
import com.github.tehras.loanapplication.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        Timber.d("Splash Screen on Start")

        if (auth.currentUser != null) {
            // already signed in
            // start HomeActivity
            this.startLoggedInActivity(auth.currentUser!!, {}, {})
        } else {
            startLoginActivity()
        }
    }

    private fun startLoginActivity() {
        // not signed in
        Timber.d("starting login activity")
        startActivity(Intent(this, LoginActivity::class.java))
    }

}