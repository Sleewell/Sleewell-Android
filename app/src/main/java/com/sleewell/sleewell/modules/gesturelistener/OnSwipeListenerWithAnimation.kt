package com.sleewell.sleewell.modules.gesturelistener

import android.animation.ValueAnimator
import android.content.Context
import android.util.TypedValue
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatValueHolder
import kotlin.math.abs


/**
 * Needs to be instantiated in setOnTouchListener of a container to detect swiping
 *
 * @constructor
 * New instance of OnSwipeListener
 *
 * @param ctx Activity context
 * @author Titouan FIANCETTE
 */
abstract class OnSwipeListenerWithAnimation(val ctx: Context?, val width: Float) : View.OnTouchListener {

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 2000
        private const val FLING_VELOCITY = 6000f
        private const val FLING_FRICTION = .85f
        const val ANIMATE_TO_START_DURATION = 200L
    }

    var animator: ValueAnimator? = null
    private var flingAnimation: FlingAnimation? = null
    private val gestureDetector = GestureDetectorCompat(ctx, GestureListener())

    init {
        gestureDetector.setIsLongpressEnabled(false)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (gestureDetector.onTouchEvent(event)) {
            return true
        }
        when (event!!.action) {
            MotionEvent.ACTION_UP -> onAnimationFinished(event.x, true)
        }
        return true
    }

    private inner class GestureListener : SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent,
            distanceX: Float, distanceY: Float
        ): Boolean {
            cancelAnimations()
            onAnimationProgress(e2.x - e1.x)
            return true
        }

        /**
         * Detection of a swipe of any kind
         *
         * @param e1
         * @param e2
         * @param velocityX
         * @param velocityY
         * @return Whether or not there was a swipe of any kind
         */
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x

                var flingVelocity = FLING_VELOCITY
                if (velocityX < 0) {
                    flingVelocity = -flingVelocity
                }
                val pixelVelocityX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, flingVelocity, ctx?.resources?.displayMetrics)

                if (abs(diffX) > abs(diffY) && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    cancelAnimations()
                    flingAnimation = FlingAnimation(FloatValueHolder(diffX)).apply {
                        setStartVelocity(pixelVelocityX)
                        setMinValue(-width)
                        setMaxValue(width)
                        minimumVisibleChange = FlingAnimation.MIN_VISIBLE_CHANGE_PIXELS
                        friction = FLING_FRICTION
                        addUpdateListener { _, value, _ -> onAnimationProgress(value) }
                        addEndListener { _, _, value, _ -> onAnimationFinished(value, false) }
                        start()
                    }
                }

                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD &&
                        abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
                    ) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (abs(diffY) > SWIPE_THRESHOLD &&
                    abs(velocityY) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

    fun cancelAnimations() {
        animator?.cancel()
        flingAnimation?.cancel()
    }

    abstract fun onAnimationProgress(currentX: Float)
    abstract fun onAnimationFinished(finalX: Float, up: Boolean)

    /**
     * Callback to override, executed on swipe right event
     * @author Titouan FIANCETTE
     */
    abstract fun onSwipeRight()
    /**
     * Callback to override, executed on swipe left event
     * @author Titouan FIANCETTE
     */
    abstract fun onSwipeLeft()
    /**
     * Callback to override, executed on swipe right event
     * @author Titouan FIANCETTE
     */
    abstract fun onSwipeTop()
    /**
     * Callback to override, executed on swipe right event
     * @author Titouan FIANCETTE
     */
    abstract fun onSwipeBottom()
}