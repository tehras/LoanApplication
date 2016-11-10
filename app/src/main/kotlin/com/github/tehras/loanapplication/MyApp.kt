package com.github.tehras.loanapplication

import android.app.Application
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by tehras on 11/5/16.
 */

class MyApp : Application() {

    @Inject
    lateinit var debugTree: Timber.DebugTree

    companion object {
        lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        initDependencyGraph()

        if (BuildConfig.DEBUG) {
            Timber.plant(debugTree)
        }
    }

    private fun initDependencyGraph() {
        graph = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        graph.injectTo(this)
    }
}