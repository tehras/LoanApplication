package com.github.tehras.loanapplication.ui.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.BottomSheetDialogFragment
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.MyApp

/**
 * Created by tehras on 11/5/16.
 */
abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies(MyApp.graph)
    }

    abstract fun injectDependencies(graph: AppComponent)
}