package com.sleewell.sleewell.mvp.menu.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sleewell.sleewell.mvp.menu.alarm.view.AlarmsFragment
import com.sleewell.sleewell.mvp.menu.home.view.HomeFragment
import com.sleewell.sleewell.mvp.menu.profile.view.ProfileContainerFragment
import com.sleewell.sleewell.mvp.menu.routine.view.RoutineFragment
import com.sleewell.sleewell.mvp.menu.settings.view.SettingsFragment
import com.sleewell.sleewell.mvp.menu.statistics.view.StatFragment

class MenuFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> AlarmsFragment()
            2 -> ProfileContainerFragment()
            3 -> RoutineFragment()
            4 -> StatFragment()
            5 -> SettingsFragment()
            else -> HomeFragment()
        }
    }
}
