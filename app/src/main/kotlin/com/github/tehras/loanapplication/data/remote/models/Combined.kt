package com.github.tehras.loanapplication.data.remote.models

import nz.bradcampbell.paperparcel.PaperParcel
import java.util.*

@PaperParcel
data class PaymentsAndLoans(
        var paymentsResponse: PaymentsResponse,
        var loans: ArrayList<Loan>
)
