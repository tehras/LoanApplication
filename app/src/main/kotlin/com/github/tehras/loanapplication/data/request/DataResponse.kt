package com.github.tehras.loanapplication.data.request

class DataResponse<T> {

    var status: Status = Status.STARTED
    var response: T? = null

    enum class Status {
        STARTED, CACHE, NETWORK
    }

    override fun toString(): String {
        return "DataResponse(status=$status, response=$response)"
    }

}