package com.github.tehras.loanapplication.extensions

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Add to bundle
 */
fun <T : Fragment> T.addToBundle(f: Bundle.() -> Unit): T {
    val bundle: Bundle = Bundle()
    bundle.f()

    this.arguments = bundle
    return this
}
