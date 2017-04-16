package com.github.tehras.loanapplication.ui.base.rx

import android.support.annotation.CallSuper
import com.github.tehras.loanapplication.ui.base.AbstractPresenter
import com.github.tehras.loanapplication.ui.base.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject


abstract class RxPresenter<V : MvpView> : AbstractPresenter<V>() {

    protected val subscriptions: CompositeDisposable = CompositeDisposable()
    private val viewState: BehaviorSubject<Boolean> = BehaviorSubject.create()

    init {
        viewState.onNext(false)
    }

    @CallSuper
    override fun bindView(view: V) {
        super.bindView(view)
        viewState.onNext(true)
    }

    @CallSuper
    override fun unbindView() {
        super.unbindView()
        viewState.onNext(false)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        viewState.onComplete()
        clearSubscriptions()
    }

    private fun clearSubscriptions() {
        subscriptions.clear()
    }

    fun addSubscription(subscription: Disposable) {
        subscriptions.add(subscription)
    }

}
