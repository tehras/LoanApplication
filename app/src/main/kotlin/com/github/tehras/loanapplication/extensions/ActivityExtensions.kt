package com.github.tehras.loanapplication.extensions

import android.animation.Animator
import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_add_loan.*

fun View.centerX(): Int {
    return (this.left + this.right) / 2
}

fun View.centerY(): Int {
    return (this.top + this.bottom) / 2
}

fun AppCompatActivity.getTopFragment() : Fragment? {
    return this.supportFragmentManager?.fragments?.get(supportFragmentManager?.fragments?.size?.minus(1) ?: 0)
}

/**
 * This will take the X, Y coordinate and animate the view back where it came from
 *
 * @param cx <-- center X of the view
 * @param cy <-- center Y of the view
 */
fun Activity.exitCircularReveal(cx: Int, cy: Int) {
    var radius = 0

    if (intent.extras != null)
        radius = intent.getIntExtra(EXTRA_RADIUS_COORDINATE, radius) / 2 // the divided by 2 will make it seem like it's going into the button a little more


    val startRadius = Math.max(root_view.width, root_view.height).toFloat()

    // create the animator for this view (the start radius is zero)
    val circularReveal = ViewAnimationUtils.createCircularReveal(root_view, cx, cy, startRadius, radius.toFloat())
    circularReveal.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
    circularReveal.addListener(object : Animator.AnimatorListener {

        override fun onAnimationEnd(p0: Animator?) {
            root_view.visibility = View.INVISIBLE
            this@exitCircularReveal.finish()
        }

        override fun onAnimationRepeat(p0: Animator?) {

        }

        override fun onAnimationStart(p0: Animator?) {

        }

        override fun onAnimationCancel(p0: Animator?) {

        }
    })

    // make the view visible and start the animation
    root_view.visibility = View.VISIBLE
    circularReveal.start()
}


/**
 * This will take the X, Y coordinate and animate the view back where it came from
 *
 * X coordinate has to be labeled as #EXTRA_X_COORDINATE in the Extra of the Intent
 * Y coordinate has to be labeled as #EXTRA_Y_COORDINATE in the Extra of the Intent
 */
fun Activity.exitCircularReveal() {
    val extras = intent.extras

    var cx = root_view.width / 2 //final cx
    var cy = root_view.height / 2 //final cy

    if (extras != null) {
        cx = intent.getIntExtra(EXTRA_X_COORDINATE, cx)
        cy = intent.getIntExtra(EXTRA_Y_COORDINATE, cy)
    }

    exitCircularReveal(cx, cy)
}

/**
 * This will get the X and Y Coordinates from the intent and will use that to animate
 *
 * X coordinate has to be labeled as #EXTRA_X_COORDINATE in the Extra of the Intent
 * Y coordinate has to be labeled as #EXTRA_Y_COORDINATE in the Extra of the Intent
 */
fun Activity.enterCircularReveal() {
    root_view.visibility = View.INVISIBLE

    val viewTreeObserver = root_view.viewTreeObserver
    if (viewTreeObserver.isAlive) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                circularRevealActivity(root_view)
                root_view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

            private fun circularRevealActivity(root_view: LinearLayout) {
                val extras = intent.extras

                var cx = root_view.width / 2
                var cy = root_view.height / 2
                var radius = 0

                if (extras != null) {
                    cx = intent.getIntExtra(EXTRA_X_COORDINATE, cx)
                    cy = intent.getIntExtra(EXTRA_Y_COORDINATE, cy)
                    radius = intent.getIntExtra(EXTRA_RADIUS_COORDINATE, radius)
                }
                val finalRadius = Math.max(root_view.width, root_view.height).toFloat()

                // create the animator for this view (the start radius is zero)
                val circularReveal = ViewAnimationUtils.createCircularReveal(root_view, cx, cy, radius.toFloat(), finalRadius)
                circularReveal.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

                // make the view visible and start the animation
                root_view.visibility = View.VISIBLE
                circularReveal.start()
            }
        })
    }
}