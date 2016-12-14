package com.github.tehras.loanapplication.data.remote.models

import com.google.gson.annotations.SerializedName
import nz.bradcampbell.paperparcel.PaperParcel
import nz.bradcampbell.paperparcel.PaperParcelable
import java.util.*

@PaperParcel
data class PaymentsResponse(
        @SerializedName("AggregatedPaymentList")
        var payments: ArrayList<Payment>?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelable.Creator(PaymentsResponse::class.java)
    }
}

@PaperParcel
data class SinglePaymentsResponses(
        var singlePaymentResponses: ArrayList<SinglePaymentResponse>
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelable.Creator(SinglePaymentsResponses::class.java)
    }
}

@PaperParcel
data class SinglePaymentResponse(
        @SerializedName("PaymentList")
        var payments: ArrayList<Payment>?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelable.Creator(SinglePaymentResponse::class.java)
    }
}

@PaperParcel
data class Payment(
        @SerializedName("Balance")
        var balance: Double,
        @SerializedName("Payment")
        var payment: Double,
        @SerializedName("Interest")
        var interest: Double,
        @SerializedName("Principal")
        var principal: Double,
        @SerializedName("PaymentDate")
        var paymentDate: String
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelable.Creator(Payment::class.java)
    }
}
