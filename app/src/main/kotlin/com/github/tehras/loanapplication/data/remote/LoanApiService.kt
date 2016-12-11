package com.github.tehras.loanapplication.data.remote

import com.github.tehras.loanapplication.data.remote.models.Loan
import com.github.tehras.loanapplication.data.remote.models.PaymentsResponse
import com.github.tehras.loanapplication.data.remote.models.SinglePaymentResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable
import rx.Single
import java.util.*

interface LoanApiService {

    @GET("/loans")
    fun loanSearch(): Single<ArrayList<Loan>>

    @GET("/payments")
    fun retrievePayments(): Single<PaymentsResponse>

    @GET("/payments/{loanId}")
    fun retrieveSingleRepayments(@Path(value = "loanId", encoded = true) loanId: String): Single<ArrayList<SinglePaymentResponse>>

    @POST("/loans")
    fun submitLoan(@Body loan: Loan): Observable<Loan>
}