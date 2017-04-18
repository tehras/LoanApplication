package com.github.tehras.loanapplication.data.remote.models

import java.io.Serializable
import java.util.*

data class PaymentsAndLoans(
        var paymentsResponse: PaymentsResponse,
        var loans: ArrayList<Loan>
) : Serializable
