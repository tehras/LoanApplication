package com.github.tehras.loanapplication.ui.addloan.fragments.other

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import timber.log.Timber
import java.text.DecimalFormat

/**
 * Add Loan balance Edit Text Watcher
 * This will be used to format balance
 */
class AddLoanPercentageEditTextWatcher(var editText: EditText) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    private var current: String = ""

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (!s.toString().equals(current, true)) {
            editText.removeTextChangedListener(this)

            val cleanString = s?.replace(Regex("[%.]"), "") ?: ""

            var formatted = ""

            if (!cleanString.isEmpty()) {
                val parsed = cleanString.toDouble()
                Timber.d("parsed $parsed")
                formatted = DecimalFormat("###.00").format((parsed / 100)) + "%"
            }
            current = formatted
            editText.setText(formatted)

            if (formatted.isNotEmpty())
                editText.setSelection(formatted.length - 1)

            editText.addTextChangedListener(this)
        }
    }

}

