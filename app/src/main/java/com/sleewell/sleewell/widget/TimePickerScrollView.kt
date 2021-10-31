package com.sleewell.sleewell.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TimePicker

class TimePickerScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TimePicker(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.actionMasked === MotionEvent.ACTION_DOWN) {
            val p = parent
            p?.requestDisallowInterceptTouchEvent(true)
        }
        return false
    }
}