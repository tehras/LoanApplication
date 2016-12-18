package com.github.tehras.loanapplication.extensions

import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.tehras.loanapplication.R

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun ViewGroup.show() {
    visibility = View.VISIBLE
}

fun ViewGroup.hide() {
    visibility = View.GONE
}

fun AppBarLayout.setupElevationListener(toolbar: Toolbar) {
    this.addOnOffsetChangedListener { appBarLayout, i ->
        if (Math.abs(i) > 0)
            toolbar.elevateToolbar()
        else
            toolbar.removeToolbarElevation()
    }
}

private fun Toolbar.removeToolbarElevation() {
    this.elevation = 0.toFloat()
}


fun Toolbar.elevateToolbar() {
    this.elevation = this.context.resources.getDimension(R.dimen.large_elevation)
}

fun Context.inflateLayout(layoutResId: Int): View {
    return inflateView(this, layoutResId, null, false)
}

fun Context.inflateLayout(layoutResId: Int, parent: ViewGroup): View {
    return inflateLayout(layoutResId, parent, true)
}

fun Context.inflateLayout(layoutResId: Int, parent: ViewGroup, attachToRoot: Boolean): View {
    return inflateView(this, layoutResId, parent, attachToRoot)
}

private fun inflateView(context: Context, layoutResId: Int, parent: ViewGroup?, attachToRoot: Boolean): View {
    return LayoutInflater.from(context).inflate(layoutResId, parent, attachToRoot)
}

fun View.showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG, f: (Snackbar.() -> Unit) = {}) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun View.showSnackbar(@StringRes message: Int, length: Int = Snackbar.LENGTH_LONG, f: (Snackbar.() -> Unit) = {}) {
    showSnackbar(resources.getString(message), length, f)
}

fun Snackbar.action(action: String, @ColorInt color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun View.getInteger(i: Int): Int {
    return this.context.resources.getInteger(i)
}

fun ImageView.populateDrawable(i: Int) {
    visibility = View.VISIBLE
    this.setImageDrawable(this.resources.getDrawable(i, null))
    this.animateExpand()
}

@Suppress("DEPRECATION")
fun View.filterBackground(@DrawableRes color: Int) {
    this.background.mutate().setColorFilter(resources.getColor(color), PorterDuff.Mode.SRC_ATOP)
}

@Suppress("DEPRECATION")
fun AlertDialog.setButtonColors(@ColorInt positiveColor: Int, @ColorInt negativeColor: Int): AlertDialog {
    this.getButton(android.content.DialogInterface.BUTTON_POSITIVE).setTextColor(this.context.resources.getColor(positiveColor))
    this.getButton(android.content.DialogInterface.BUTTON_NEGATIVE).setTextColor(this.context.resources.getColor(negativeColor))

    return this
}
