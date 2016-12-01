package com.github.tehras.loanapplication.ui.addloan.fragments.balance

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat

/**
 * Add Loan balance Edit Text Watcher
 * This will be used to format balance
 */
class AddLoanBalanceEditTextWatcher(var editText: EditText) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    private var current: String = ""

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (!s.toString().equals(current, true)) {
            editText.removeTextChangedListener(this)

            val cleanString = s?.replace(Regex("[$,.]"), "") ?: ""

            val parsed = cleanString.toDouble()
            val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))

            current = formatted
            editText.setText(formatted)
            editText.setSelection(formatted.length)

            editText.addTextChangedListener(this)
        }
    }

}

