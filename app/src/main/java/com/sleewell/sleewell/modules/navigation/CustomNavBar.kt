package com.sleewell.sleewell.modules.navigation

import android.util.Log
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.sleewell.sleewell.R

class CustomNavBar : ICustomNavBar {
    private val buttons = mutableListOf<ToggleButton>()
    private val labels = mutableListOf<String>()
    private val destinationIDs = mutableListOf<Int>()
    private var size = 0
    private val goRight = NavOptions.Builder()
        .setExitAnim(R.anim.slide_out_to_left)
        .setEnterAnim(R.anim.slide_in_from_right)
    private val goLeft = NavOptions.Builder()
        .setExitAnim(R.anim.slide_out_to_right)
        .setEnterAnim(R.anim.slide_in_from_left)

    override fun addButton(button: ToggleButton, label: String, destinationID: Int) {
        button.isChecked = size == 0
        button.isEnabled = !button.isChecked

        buttons.add(button)
        labels.add(label)
        destinationIDs.add(destinationID)
        size += 1
    }

    override fun setNavigation(navController: NavController) {
        buttons.forEachIndexed { index, button ->
            button.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
                button.isEnabled = !b
                if (b) {
                    Log.d("StateMAGIQUE", index.toString())
                    if (labels.indexOf(navController.currentDestination?.label.toString()) > index) {
                        navController.navigate(destinationIDs[index], null, goLeft.build())
                    } else {
                        navController.navigate(destinationIDs[index], null, goRight.build())
                    }

                    buttons.forEachIndexed{i, but -> if (i != index) but.isChecked = false }
                }
            }
        }
    }
}