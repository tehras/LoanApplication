package com.github.tehras.loanapplication.data.remote.models

import com.google.gson.annotations.SerializedName
import nz.bradcampbell.paperparcel.PaperParcel
import nz.bradcampbell.paperparcel.PaperParcelable

@PaperParcel
data class Loan(
        @SerializedName("ID")
        var key: String,

        @SerializedName("Name")
        var name: String,

        @SerializedName("Provider")
        var provider: String,

        @SerializedName("Balance")
        var balance: Double,

        @SerializedName("Interest")
        var interest: Double,

        @SerializedName("BasePayment")
        var payment: Double,

        @SerializedName("ExtraPayment")
        var extraPayment: Double,

        @SerializedName("RepaymentStartDate")
        var repaymentStartDate: String
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelable.Creator(Loan::class.java)
    }
}