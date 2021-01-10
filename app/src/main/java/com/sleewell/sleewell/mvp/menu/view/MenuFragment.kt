package com.sleewell.sleewell.mvp.menu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.navigation.fragment.NavHostFragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.navigation.CustomNavBar

class MenuFragment : Fragment() {
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.new_fragment_menu, container, false)
        initActivityWidgets()

        return root
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.nav_menu) as? NavHostFragment
        val navController = nestedNavHostFragment?.navController

        val homeNav = root.findViewById<ToggleButton>(R.id.home_nav)
        val settingsNav = root.findViewById<ToggleButton>(R.id.settings_nav)
        val alarmNav = root.findViewById<ToggleButton>(R.id.alarm_nav)
        val statNav = root.findViewById<ToggleButton>(R.id.stats_nav)

        val customNavBar = CustomNavBar()
        customNavBar.addButton(homeNav, getString(R.string.home_label), R.id.homeFragment)
        customNavBar.addButton(settingsNav, getString(R.string.settings_label), R.id.settingsFragment)
        customNavBar.addButton(alarmNav, getString(R.string.alarm_label), R.id.alarmFragment)
        customNavBar.addButton(statNav, getString(R.string.statistics_label), R.id.statFragment)

        customNavBar.setNavigation(navController!!)
    }
}