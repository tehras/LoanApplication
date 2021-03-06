package com.github.tehras.loanapplication.extensions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Add to bundle
 */
fun <T : Fragment> T.addToBundle(f: Bundle.() -> Unit): T {
    val bundle: Bundle = Bundle()
    bundle.f()

    this.arguments = bundle
    return this
}

fun Fragment.startFragment(view: Int, activity: AppCompatActivity) {
    startFragment(view, activity, false)
}

fun Fragment.startFragment(view: Int, activity: AppCompatActivity, addToStack: Boolean) {
    val tran = activity.supportFragmentManager
            .beginTransaction()
            .replace(view, this)
    if (addToStack) tran.addToBackStack(this.javaClass.simpleName)
    tran.commit()
}
