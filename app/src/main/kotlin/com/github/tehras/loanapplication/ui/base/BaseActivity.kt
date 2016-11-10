package com.github.tehras.loanapplication.ui.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.github.tehras.loanapplication.AppComponent
import com.github.tehras.loanapplication.MyApp

abstract class BaseActivity : AppCompatActivity() {
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies(MyApp.graph)
    }

    abstract fun injectDependencies(graph: AppComponent)
}