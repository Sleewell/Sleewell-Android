package com.sleewell.sleewell.modules.gesturelistener

import android.app.Activity
import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
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
abstract class OnSwipeListener(ctx: Context?) : View.OnTouchListener {

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    private val gestureDetector: GestureDetectorCompat
    init {
        gestureDetector = GestureDetectorCompat(ctx, GestureListener())
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
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
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > Companion.SWIPE_THRESHOLD && abs(
                            velocityX
                        ) > Companion.SWIPE_VELOCITY_THRESHOLD
                    ) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (abs(diffY) > Companion.SWIPE_THRESHOLD && abs(
                        velocityY
                    ) > Companion.SWIPE_VELOCITY_THRESHOLD
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