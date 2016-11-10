package com.github.tehras.loanapplication.ui.base

import android.content.Context
import android.support.v4.app.Fragment
import com.github.tehras.loanapplication.ui.FragmentScope
import dagger.Module
import dagger.Provides

@Module
abstract class FragmentModule(private val fragment: Fragment) {

    @Provides @FragmentScope
    fun provideFragment(): Fragment = fragment

    @Provides @FragmentScope
    fun provideFragmentContext(): Context = fragment.activity.baseContext
}
