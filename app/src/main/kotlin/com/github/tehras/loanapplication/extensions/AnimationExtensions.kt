package com.github.tehras.loanapplication.extensions

import android.animation.Animator
import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator

private val defaultAnimationTime = 150L
/**
 * Default animator
 *
 * animationTime = 0L
 * postAnimation = 0L
 * interpolator = TimeInterpolator
 */
val defaultAnimBuilder = AnimationBuilder.Builder.animationTime(defaultAnimationTime).postAnimation(0).build()


class AnimationBuilder private constructor(
        val postAnimtion: Long,
        val animationTime: Long,
        val interpolator: TimeInterpolator, val func: View.() -> Unit) {

    companion object Builder {

        private var postAnimation: Long = 0L
        private var func: View.() -> Unit = {}
        private var animationTime: Long = 150L
        private var interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()

        fun postAnimation(postAnim: Long): Builder {
            postAnimation = postAnim
            return this
        }

        fun animationTime(animTime: Long): Builder {
            animationTime = animTime / 2
            return this
        }

        @Suppress("UNUSED")
        fun interpolator(inter: TimeInterpolator): Builder {
            interpolator = inter
            return this
        }

        fun postAnimFunction(func: View.() -> Unit): Builder {
            this.func = func
            return this
        }

        fun build(): AnimationBuilder {
            return AnimationBuilder(postAnimation, animationTime, interpolator, func)
        }
    }

    override fun toString(): String {
        return "AnimationBuilder(postAnimtion=$postAnimtion, animationTime=$animationTime, interpolator=$interpolator, func=$func)"
    }
}

/**
 * Animation that will slide the view from left to right
 */
fun View.animateInFromLeft(builder: AnimationBuilder) {
    this.translationX = this.width.toFloat()
    this.animate().setDuration(builder.animationTime)
            .setInterpolator(builder.interpolator)
            .translationXBy(-this.width.toFloat())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {

                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationStart(p0: Animator?) {
                    this@animateInFromLeft.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(p0: Animator?) {
                    this@animateInFromLeft.let {
                        builder.func(it)
                    }
                }

            })
            .setStartDelay(builder.postAnimtion)
            .start()
}

/**
 * Animation that will slide the view from left to right
 */
fun View.animateInFromRight(builder: AnimationBuilder) {
    this.translationX = -this.width.toFloat()
    this.animate().setDuration(builder.animationTime)
            .setInterpolator(builder.interpolator)
            .translationXBy(this.width.toFloat())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {

                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationStart(p0: Animator?) {
                    this@animateInFromRight.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(p0: Animator?) {
                    this@animateInFromRight.let {
                        builder.func(it)
                    }
                }

            })
            .setStartDelay(builder.postAnimtion)
            .start()
}

fun View.waitForLayoutToFinish(func: View.() -> Unit) {
    if (this.viewTreeObserver.isAlive) {
        this.visibility = View.INVISIBLE
        this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                this@waitForLayoutToFinish.visibility = View.VISIBLE
                this@waitForLayoutToFinish.func()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}

fun View.animateInFromTop(builder: AnimationBuilder) {
    this.animateVertically(builder, -this.measuredHeight.toFloat())
}

fun View.animateInFromBottom(builder: AnimationBuilder) {
    this.animateVertically(builder, this.measuredHeight.toFloat())
}

fun View.animateExpand() {
    this.scaleX = 0.3f
    this.scaleY = 0.3f
    this.animate()
            .scaleXBy(0.7f)
            .scaleYBy(0.7f)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {

                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                    this@animateExpand.visibility = View.VISIBLE
                }

            })
            .start()
}

private fun View.animateVertically(builder: AnimationBuilder, translationY: Float) {
    this.translationY = translationY
    this.animate().setDuration(builder.animationTime)
            .translationYBy(-translationY)
            .setInterpolator(builder.interpolator)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    builder.func(this@animateVertically)
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationStart(p0: Animator?) {
                    this@animateVertically.visibility = View.VISIBLE
                }

            })
            .setStartDelay(builder.postAnimtion)
            .start()
}