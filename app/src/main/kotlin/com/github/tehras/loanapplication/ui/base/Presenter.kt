package com.github.tehras.loanapplication.ui.base

interface Presenter<in V : MvpView> {

    fun bindView(view: V)

    fun unbindView()

    fun onDestroy()
}
