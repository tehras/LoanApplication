package com.github.tehras.loanapplication.ui.addloan

import com.github.tehras.loanapplication.R

enum class AddLoanStage(var stage: Int, var bottomGrad: Int) {
    BASIC_INFORMATION(0, R.drawable.backpack), BALANCE_INFORMATION(1, R.drawable.calculator);

    companion object {
        fun convertToStage(i: Int): AddLoanStage {
            values().forEach {
                if (i.compareTo(it.stage) == 0) {
                    return it
                }
            }

            return BASIC_INFORMATION
        }
    }

}