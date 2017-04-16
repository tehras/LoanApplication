package com.github.tehras.loanapplication.ui.base.rx.transformers

import com.github.tehras.loanapplication.ui.base.rx.RxAction
import com.github.tehras.loanapplication.ui.base.rx.RxResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer


class RxTransformer<T : RxAction, S : RxResult> : ObservableTransformer<T, S> {

    override fun apply(observable: Observable<T>?): ObservableSource<S>? {
        return ObservableSource {

        }
    }

//    override fun call(observable: rx.Observable<T>): rx.Observable<T> {
//
//        // This is nearly identical to DeliverLatest except we call take(1) on the data observable first
//        return rx.Observable.combineLatest(
//                view,
//                // Emit only first value from data Observable
//                observable.take(1)
//                        // Use materialize to propagate onError events from data Observable
//                        // only after view Observable emits true
//                        .materialize()
//                        .delay { notification ->
//                            // Delay completed notifications until the view reattaches
//                            if (notification.isOnCompleted) {
//                                view.first { it }
//                            } else {
//                                // Pass all other events downstream immediately
//                                // They will be "cached" by combineLatest
//                                rx.Observable.empty()
//                            }
//                        }
//        ) { flag, notification ->
//            if (flag) notification else null
//        }
//                .dematerialize()
//    }
}