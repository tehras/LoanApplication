package com.github.tehras.loanapplication.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

/**
 * Error checker for minimum and maximum
 */
fun TextView.isValidLength(min: Int, max: Int): Boolean {
    if (this.text.length < min) {
        this.error = "Text must be at least $min characters long"
        return false
    }

    if (this.text.length >= max) {
        this.error = "Text must be less than $max characters long"
        return false
    }

    return true
}

/**
 * Error checker for minimum and maximum $$
 */
fun TextView.isValidPercentage(min: Double, max: Double): Boolean {
    //get the percentage
    val d = this.convertToDouble()

    if (d < min) {
        this.error = "The percentage must be at least $$min"
        return false
    }

    if (d >= max) {
        this.error = "The percentage must be less than $$max"
        return false
    }

    return true
}

/**
 * Error checker for minimum and maximum $$
 */
fun TextView.isValidAmount(min: Double, max: Double): Boolean {
    //get the amount
    val d = this.convertToDouble()

    if (d < min) {
        this.error = "The amount must be at least $$min"
        return false
    }

    if (d >= max) {
        this.error = "The amount must be less than $$max"
        return false
    }

    return true
}

fun TextView.convertToDouble(): Double {
    val s = this.text.replace(Regex("[%$,.]"), "")
    if (s.isEmpty()) return 0.00
    return s.toDouble()/100
}

/**
 * This is the method that will add the text watcher and allow
 * to set a cusotm funciton on afterTextChanged
 */
fun TextView.addErrorTextWatcher(func: TextView.() -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            this@addErrorTextWatcher.func()
        }

    })

}