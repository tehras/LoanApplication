package com.github.tehras.loanapplication.extensions

import android.os.Bundle
import android.os.Parcelable
import timber.log.Timber


fun <T : Parcelable> Bundle.getSafeParcelable(bunldeId: String): T? {
    try {
        return this.getParcelable(bunldeId)
    } catch (e: IllegalStateException) {
        Timber.d(e.message)
    }
    return null
}

val EXTRA_X_COORDINATE = "ext_x_coordinate"
val EXTRA_Y_COORDINATE = "ext_y_coordinate"
val EXTRA_RADIUS_COORDINATE = "ext_radisu_coordinate"