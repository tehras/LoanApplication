package com.github.tehras.loanapplication.ui.addloan.fragments.other

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.*


/**
 * Add Loan balance Edit Text Watcher
 * This will be used to format balance
 */
class AddLoanDateEditTextWatcher(var date: EditText) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    private var current: String = ""
    private val ddmmyyyy = "mmddyyyy"
    private val cal = Calendar.getInstance()

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (s.toString() != current) {
            var clean = s?.replace(Regex("[^\\d.]"), "") ?: ""
            val cleanC = current.replace(Regex("[^\\d.]"), "")

            val cl = clean.length
            var sel = cl
            var i = 2
            while (i <= cl && i < 6) {
                sel++
                i += 2
            }
            //Fix for pressing delete next to a forward slash
            if (clean == cleanC) sel--

            if (clean.length < 8) {
                clean += ddmmyyyy.substring(clean.length)
            } else {
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                var mon = Integer.parseInt(clean.substring(0, 2))
                var day = Integer.parseInt(clean.substring(2, 4))
                var year = Integer.parseInt(clean.substring(4, 8))

                if (mon > 12) mon = 12
                cal.set(Calendar.MONTH, mon - 1)
                year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                cal.set(Calendar.YEAR, year)
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                clean = String.format("%02d%02d%02d", mon, day, year)
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8))

            sel = if (sel < 0) 0 else sel
            current = clean
            date.setText(current)
            date.setSelection(if (sel < current.length) sel else current.length)
        }
    }

}

