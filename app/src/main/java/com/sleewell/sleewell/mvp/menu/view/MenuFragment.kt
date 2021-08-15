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
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.OnSwipeListenerWithAnimation
import com.sleewell.sleewell.modules.navigation.CustomNavBar
import com.sleewell.sleewell.mvp.menu.MenuContract
import com.sleewell.sleewell.mvp.menu.presenter.MenuPresenter


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
    @SuppressLint("ClickableViewAccessibility")
    private fun initActivityWidgets() {
        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_menu) as? NavHostFragment
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

        layout.setOnTouchListener(object : OnSwipeListenerWithAnimation(activity!!.applicationContext,
            getScreenWidth(activity!!).toFloat()) {
            override fun onSwipeTop() {}
            override fun onSwipeBottom() {}

            override fun onSwipeLeft() {
                customNavBar.navigateRight(navController)
            }

            override fun onSwipeRight() {
                customNavBar.navigateLeft(navController)
            }

            override fun onAnimationProgress(currentX: Float) {
                val translation = calculateTranslation(currentX)
                nestedNavHostFragment.view?.translationX = translation.toFloat()
            }

            override fun onAnimationFinished(finalX: Float, up: Boolean) {
                cancelAnimations()
                animator = if (up) {
                    ValueAnimator.ofFloat(nestedNavHostFragment.view!!.translationX * 10, 0f)
                } else {
                    ValueAnimator.ofFloat(finalX, 0f)
                }
                animator?.addUpdateListener { valueAnimator -> onAnimationProgress(valueAnimator.animatedValue as Float) }
                animator?.duration = ANIMATE_TO_START_DURATION
                animator?.start()
            }
        })
    }

    private fun calculateTranslation(x: Float): Int {
        return x.toInt() / 10
    }

    override fun setPresenter(presenter: MenuContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }
}