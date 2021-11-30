package com.sleewell.sleewell.mvp.menu.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.sleewell.sleewell.R

class ProfileContainerFragment : Fragment() {
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile_container, container, false)


        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_profile) as? NavHostFragment
        // nestedNavHostFragment?.navController?.navigate(R.id.profileFragment)

        return root
    }
}