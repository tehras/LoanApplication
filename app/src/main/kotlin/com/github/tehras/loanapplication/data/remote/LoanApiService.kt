package com.github.tehras.loanapplication.data.remote

import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.PaymentsResponse
import com.github.tehras.loanapplication.data.remote.models.SinglePaymentResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface LoanApiService {

    @GET("/loans")
    fun loanSearch(): Single<ArrayList<Loan>>

    @GET("/payments")
    fun retrievePayments(): Single<PaymentsResponse>

    @GET("/payments/{loanId}")
    fun retrieveSingleRepayments(@Path(value = "loanId", encoded = true) loanId: String): Single<ArrayList<SinglePaymentResponse>>

    @POST("/loans")
    fun submitLoan(@Body loan: Loan): io.reactivex.Observable<Response<Void>>

    @DELETE("/loans/{loanId}")
    fun deleteLoan(@Path(value = "loanId", encoded = true) loanId: String): io.reactivex.Observable<Response<Void>>
}