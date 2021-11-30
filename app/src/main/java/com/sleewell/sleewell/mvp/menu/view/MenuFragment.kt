package com.sleewell.sleewell.mvp.menu.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.OnSwipeListenerWithAnimation
import com.sleewell.sleewell.modules.navigation.CustomNavBar
import com.sleewell.sleewell.mvp.menu.MenuContract
import com.sleewell.sleewell.mvp.menu.presenter.MenuPresenter


class MenuFragment : Fragment(), MenuContract.View {

    private lateinit var root: View
    private lateinit var presenter: MenuContract.Presenter
    private lateinit var demoCollectionAdapter: MenuFragmentStateAdapter
    private lateinit var viewPager: ViewPager2
    private var firstTime = true;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        root = inflater.inflate(R.layout.new_fragment_menu, container, false)
        initActivityWidgets()
        setPresenter(MenuPresenter(this, this.activity as AppCompatActivity))
        return root
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        demoCollectionAdapter = MenuFragmentStateAdapter(this)
        viewPager = root.findViewById(R.id.pager)
        viewPager.adapter = demoCollectionAdapter

        val tabLayout = root.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> context?.getString(R.string.nav_button_home)
                1 -> context?.getString(R.string.alarm_menu)
                2 -> context?.getString(R.string.profile_menu)
                3 -> context?.getString(R.string.routine_menu)
                4 -> context?.getString(R.string.nav_button_stats)
                5 -> context?.getString(R.string.nav_button_settings)
                else -> context?.getString(R.string.nav_button_home)
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    if ((it == 4 || it == 3) && firstTime) {
                        viewPager.setCurrentItem(it, false)
                    } else {
                        viewPager.setCurrentItem(it, true)
                    }
                    firstTime = false
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun setPresenter(presenter: MenuContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }
}