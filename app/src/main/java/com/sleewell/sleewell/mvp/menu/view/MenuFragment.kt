package com.sleewell.sleewell.mvp.menu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.OnSwipeListener
import com.sleewell.sleewell.modules.navigation.CustomNavBar
import com.sleewell.sleewell.mvp.menu.MenuContract
import com.sleewell.sleewell.mvp.menu.presenter.MenuPresenter
import kotlinx.android.synthetic.main.activity_protocol_container.*
import kotlinx.android.synthetic.main.activity_protocol_container.protocolLayout
import kotlinx.android.synthetic.main.new_fragment_menu.*

class MenuFragment : Fragment(), MenuContract.View {

    private lateinit var root: View
    private lateinit var presenter: MenuContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.new_fragment_menu, container, false)
        initActivityWidgets()
        setPresenter(MenuPresenter(this, this.activity as AppCompatActivity))

        return root
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.nav_menu) as? NavHostFragment
        val navController = nestedNavHostFragment?.navController

        val homeNav = root.findViewById<ToggleButton>(R.id.home_nav)
        val alarmNav = root.findViewById<ToggleButton>(R.id.alarm_nav)
        val profileNav = root.findViewById<ToggleButton>(R.id.profile_nav)
        val routineNav = root.findViewById<ToggleButton>(R.id.routine_nav)
        val statNav = root.findViewById<ToggleButton>(R.id.stats_nav)
        val settingsNav = root.findViewById<ToggleButton>(R.id.settings_nav)

        val layout = root.findViewById<ConstraintLayout>(R.id.constraintLayout)

        val customNavBar = CustomNavBar()

        customNavBar.addButton(homeNav, R.id.homeFragment)
        customNavBar.addButton(alarmNav, R.id.alarmFragment)
        customNavBar.addButton(profileNav, R.id.profileFragment)
        customNavBar.addButton(routineNav, R.id.routineFragment)
        customNavBar.addButton(statNav, R.id.statFragment)
        customNavBar.addButton(settingsNav, R.id.settingsFragment)

        customNavBar.setNavigation(navController!!)

        layout.setOnTouchListener(object : OnSwipeListener(activity!!.applicationContext) {
            override fun onSwipeTop() {}
            override fun onSwipeBottom() {}

            override fun onSwipeLeft() {
                println("SWIPE LEFT")
                customNavBar.navigateRight(navController)
            }

            override fun onSwipeRight() {
                println("SWIPE RIGHT")
                customNavBar.navigateLeft(navController)
            }
        })
    }

    override fun setPresenter(presenter: MenuContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }
}