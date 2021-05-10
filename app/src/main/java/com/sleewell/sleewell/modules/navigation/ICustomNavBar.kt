package com.sleewell.sleewell.modules.navigation

import android.widget.ToggleButton
import androidx.navigation.NavController

/**
 * Custom navigation bar made using ToggleButtons
 * Used for navigation between fragments
 *
 * implementation example inside a Fragment:
 * <pre>
 *    val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_menu) as? NavHostFragment
 *    val navController = nestedNavHostFragment?.navController
 *    val homeNav = root.findViewById<ToggleButton>(R.id.homeNavButton)
 *    val settingsNav = root.findViewById<ToggleButton>(R.id.settingsNavButton)
 *    val statNav = root.findViewById<ToggleButton>(R.id.statNavButton)
 *
 *    val customNavBar = CustomNavBar()
 *    customNavBar.addButton(homeNav, getString(R.string.home_label), R.id.home)
 *    customNavBar.addButton(settingsNav, getString(R.string.settings_label), R.id.settings)
 *    customNavBar.addButton(statNav, getString(R.string.statistics_label), R.id.statistic)
 *
 *    customNavBar.setNavigation(navController!!)
 * </pre>
 * @author Titouan FIANCETTE
 */
interface ICustomNavBar {

    /**
     * Add a new button to the bar
     *
     * @param button ToggleButton to add
     * @param destinationID ID in the Navigation Graph of the corresponding Fragment
     *
     * @author Titouan FIANCETTE
     */
    fun addButton(button: ToggleButton, destinationID: Int)


    /**
     * Sets the navigation and animations between the Fragments pointed to by each button
     *
     * @param navController Navigation Controller corresponding to the Navigation Graph and Nav Host Fragment
     */
    fun setNavigation(navController: NavController)
}