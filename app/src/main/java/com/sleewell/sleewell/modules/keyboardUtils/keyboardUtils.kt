@file:JvmName("KeyboardUtils")
package com.sleewell.sleewell.modules.keyboardUtils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

    fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun Fragment.hideSoftKeyboard() {
        activity?.currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(activity!!, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
