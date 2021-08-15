package com.sleewell.sleewell.modules.navigation

import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.sleewell.sleewell.R

class CustomNavBar : ICustomNavBar {
    private val buttons = mutableListOf<ToggleButton>()
    private val destinationIDs = mutableListOf<Int>()
    private var size = 0
    private val goRight = NavOptions.Builder()
        .setExitAnim(R.anim.slide_out_to_left)
        .setEnterAnim(R.anim.slide_in_from_right)
    private val goLeft = NavOptions.Builder()
        .setExitAnim(R.anim.slide_out_to_right)
        .setEnterAnim(R.anim.slide_in_from_left)

    override fun addButton(button: ToggleButton, destinationID: Int) {
        button.isChecked = size == 0
        button.isEnabled = !button.isChecked

        buttons.add(button)
        destinationIDs.add(destinationID)
        size += 1
    }

    override fun setNavigation(navController: NavController) {
        buttons.forEachIndexed { index, button ->
            button.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
                button.isEnabled = !b
                if (b) {
                    if (destinationIDs.indexOf(navController.currentDestination?.id) > index) {
                        navController.navigate(destinationIDs[index], null, goLeft.build())
                    } else {
                        navController.navigate(destinationIDs[index], null, goRight.build())
                    }

                    buttons.forEachIndexed{i, but -> if (i != index) but.isChecked = false }
                }
            }
        }
    }

    fun navigateRight(navController: NavController): Boolean {
        val index = destinationIDs.indexOf(navController.currentDestination?.id)

        if (index < destinationIDs.size - 1) {
            buttons[index].isEnabled = true
            buttons[index].isChecked = false
            buttons[index + 1].isEnabled = false
            buttons[index + 1].isChecked = true
            navController.navigate(destinationIDs[index + 1], null, goRight.build())
            return true
        }
        return false
    }

    fun navigateLeft(navController: NavController): Boolean {
        val index = destinationIDs.indexOf(navController.currentDestination?.id)

        if (index != 0) {
            buttons[index].isEnabled = true
            buttons[index].isChecked = false
            buttons[index - 1].isEnabled = false
            buttons[index - 1].isChecked = true
            navController.navigate(destinationIDs[index - 1], null, goLeft.build())
            return true
        }
        return false
    }
}