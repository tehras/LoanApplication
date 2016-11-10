package com.github.tehras.loanapplication.ui.base.rx

import android.support.annotation.CallSuper
import com.github.tehras.loanapplication.ui.base.AbstractPresenter
import com.github.tehras.loanapplication.ui.base.MvpView
import com.github.tehras.loanapplication.ui.base.rx.delivery.DeliverFirst
import com.github.tehras.loanapplication.ui.base.rx.delivery.DeliverLatest
import com.github.tehras.loanapplication.ui.base.rx.delivery.DeliverReplay
import rx.Observable
import rx.Subscription
import rx.lang.kotlin.plusAssign
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription


abstract class RxPresenter<V : MvpView> : AbstractPresenter<V>() {

    protected val subscriptions: CompositeSubscription = CompositeSubscription()
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
        viewState.onCompleted()
        clearSubscriptions()
    }

    private fun clearSubscriptions() {
        subscriptions.clear()
    }

    fun addSubscription(subscription: Subscription) {
        subscriptions += subscription
    }

    /**
     * Returns an Observable which omits the current state of the view. This observable emits
     * true when the view is attached and false when it is detached.
     */
    fun getViewState(): Observable<Boolean> = viewState.asObservable()


    /**
     * Returns an {@link rx.Observable.Transformer} that delays emission from the source {@link rx.Observable}.
     * <p>
     * {@link DeliverFirst} delivers only the first onNext value that has been emitted by the source observable.
     *
     * @param <T> the type of source observable emissions
     */
    fun <T> deliverFirst(): DeliverFirst<T> = DeliverFirst(getViewState())

    /**
     * Returns an {@link rx.Observable.Transformer} that delays emission from the source {@link rx.Observable}.
     * <p>
     * {@link DeliverLatest} keeps the latest onNext value and emits it when there is attached view.
     *
     * @param <T> the type of source observable emissions
     */
    fun <T> deliverLatest(): DeliverLatest<T> = DeliverLatest(getViewState())

    /**
     * Returns an {@link rx.Observable.Transformer} that delays emission from the source {@link rx.Observable}.
     * <p>
     * {@link DeliverReplay} keeps all onNext values and emits them each time a new view gets attached.
     *
     * @param <T> the type of source observable emissions
     */
    fun <T> deliverReplay(): DeliverReplay<T> = DeliverReplay(getViewState())

}
