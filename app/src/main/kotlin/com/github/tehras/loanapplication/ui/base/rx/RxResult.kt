package com.github.tehras.loanapplication.ui.base.rx

open class RxResult {
    val status: Status = Status.STARTED

    enum class Status {
        STARTED, CACHED_DATA, NETWORK_DATA
    }
}