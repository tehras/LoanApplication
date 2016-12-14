package com.github.tehras.loanapplication.extensions

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


fun TextView.convertToDate(): String {
    return this.convertToDate("yyyyMMdd", "MM/dd/yyyy")
}

fun TextView.convertToDate(finalFormat: String, originalFormat: String): String {
    return this.text.toString().convertToDate(finalFormat, originalFormat)
}

fun String.convertToDate(finalFormat: String, originalFormat: String): String {
    return SimpleDateFormat(finalFormat, Locale.US).format(SimpleDateFormat(originalFormat, Locale.US).parse(this))
}