package com.github.tehras.loanapplication.ui.addloan

enum class AddLoanStage(var stage: Int) {
    BASIC_INFORMATION(0);

    companion object {
        fun converToStage(i: Int): AddLoanStage {
            values().forEach {
                if (it.stage == i) return it
            }

            return BASIC_INFORMATION
        }
    }

}