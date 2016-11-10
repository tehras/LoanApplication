package com.github.tehras.loanapplication.ui.base

import android.content.Context
import android.support.design.widget.BottomSheetDialogFragment
import com.github.tehras.loanapplication.ui.FragmentScope
import dagger.Module
import dagger.Provides

@Module
abstract class BottomSheetFragmentModule(private val fragment: BottomSheetDialogFragment) {

    @Provides @FragmentScope
    fun provideFragment(): BottomSheetDialogFragment = fragment

    @Provides @FragmentScope
    fun provideFragmentContext(): Context = fragment.context
}
