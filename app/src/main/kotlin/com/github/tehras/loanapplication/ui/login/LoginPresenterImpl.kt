package com.github.tehras.loanapplication.ui.login

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.ui.base.AbstractPresenter
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber
import javax.inject.Inject

class LoginPresenterImpl @Inject constructor() : AbstractPresenter<LoginView>(), LoginPresenter, GoogleApiClient.OnConnectionFailedListener {

    private var googleApiClient: GoogleApiClient? = null
    private var auth: FirebaseAuth? = null
    private var authListener: FirebaseAuth.AuthStateListener? = null

    override fun bindView(view: LoginView) {
        super.bindView(view)

        if (view is AppCompatActivity) {
            //let's launch the google api client
            // [START config_signin]
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(view.resources.getString(R.string.default_web_client_id)).requestEmail().build()
            // [END config_signin]
            this.googleApiClient = GoogleApiClient.Builder(view).enableAutoManage(view /* FragmentActivity */, this /* OnConnectionFailedListener */).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()

            // [START initialize_auth]
            auth = FirebaseAuth.getInstance()
            // [END initialize_auth]

            Timber.d("auth $auth")

            // [START auth_state_listener]
            authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                if (user != null) {
                    // User is signed in
                    Timber.d("onAuthStateChanged:signed_in:" + user.uid)
                } else {
                    // User is signed out
                    Timber.d("onAuthStateChanged:signed_out")
                }
            }
            // [END auth_state_listener]
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_google_button -> googleSignIn()
        }
    }

    /**
     * This is called after the user clicks on the Google Login Button
     */
    private fun googleSignIn() {
        Timber.d("sign in")
        val silentSignIn = Auth.GoogleSignInApi.silentSignIn(googleApiClient)

        object : AsyncTask<Void, Void, GoogleSignInResult>() {
            override fun doInBackground(vararg p0: Void?): GoogleSignInResult {
                return silentSignIn.await()
            }

            override fun onPostExecute(result: GoogleSignInResult?) {
                if (result!!.isSuccess) {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = result.signInAccount
                    if (account != null)
                        firebaseAuthWithGoogle(account)
                } else {
                    // Google Sign In failed, update UI appropriately
                    // [START_EXCLUDE]
                    view?.sendBackFailedUse("Error authenticating, please try at a later time")
                    // [END_EXCLUDE]
                }
            }
        }.execute()
    }


    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        if (auth == null)
            return

        Timber.d("firebaseAuthWithGoogle:" + acct.id)
        // [START_EXCLUDE silent]
        view?.showLoading()
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth?.signInWithCredential(credential)?.addOnCompleteListener(view as AppCompatActivity) { task ->
            Timber.d("signInWithCredential:onComplete:" + task.isSuccessful)

            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!task.isSuccessful && auth != null) {
                view?.stopLoading()
                Timber.d("signInWithCredential", task.exception)
                view?.showAuthenticationFailed()
            } else {
                view?.sendBackSuccessUser(auth!!)
            }
        } ?: view?.sendBackFailedUse("Error authenticating, please try at a later time")
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        view?.sendBackFailedUse("Error connecting to the authentication server, please try at a later time")
    }

}