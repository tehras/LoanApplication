package com.github.tehras.loanapplication.ui.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

abstract class PresenterActivity<V : MvpView, T : Presenter<V>> : BaseActivity(),
        LoaderManager.LoaderCallbacks<T> {

    private val LOADER_ID = 1
    protected lateinit var presenter: T

    @Inject
    protected lateinit var presenterLoaderProvider: Provider<PresenterLoader<T>>

    protected var freshStart: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLoader()

        if (savedInstanceState == null) {
            this.freshStart = true
        } else {
            this.freshStart = false
        }
    }

    @CallSuper
    protected fun onPresenterProvided(presenter: T) {
        Timber.d("onPresenterProvided")
        this.presenter = presenter
    }

    @CallSuper
    protected fun onPresenterDestroyed() {
        // Hook for subclasses
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        presenter.bindView(getViewLayer())
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    protected fun getViewLayer(): V {
        @Suppress("UNCHECKED_CAST")
        return this as V
    }


    private fun initLoader() {
        Timber.d("initLoader")
        supportLoaderManager.initLoader<T>(LOADER_ID, null, this)
    }

    override fun onLoadFinished(loader: Loader<T>?, presenter: T) {
        Timber.d("onLoadFinished")
        onPresenterProvided(presenter)
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<T> {
        Timber.d("onCreateLoader")
        return presenterLoaderProvider.get()
    }

    override fun onLoaderReset(loader: Loader<T>?) {
        Timber.d("onLoaderReset")
        onPresenterDestroyed()
    }

}