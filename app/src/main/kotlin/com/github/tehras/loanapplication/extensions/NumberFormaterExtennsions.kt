package com.github.tehras.loanapplication.extensions

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Double.dollarWithTwoDecimalsFormat(): CharSequence {
    return NumberFormat.getCurrencyInstance().format(this)
}

fun Double.percentageFormat(): CharSequence {
    return NumberFormat.getPercentInstance().format(this.div(100))
}

fun String.formatDate(inDate: String, outDate: String): String? {
    try {
        val inFormat = SimpleDateFormat(inDate, Locale.US)
        val outFormat = SimpleDateFormat(outDate, Locale.US)

        return outFormat.format(inFormat.parse(this))
    } catch (e: Exception) {
        return "N/A"
    }
}
