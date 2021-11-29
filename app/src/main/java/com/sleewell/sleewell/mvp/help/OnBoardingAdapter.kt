package com.sleewell.sleewell.mvp.help

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class OnBoardingAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val total: Int = 6
    private val data : ArrayList<PageFragment> = ArrayList(total)

    init {
        for (i in 0 until total) {
            val fragment = PageFragment()
            val bundle = Bundle()
            bundle.putInt("POSITION", i + 1)
            fragment.arguments = bundle
            data.add(fragment)
        }
    }

    override fun getItem(position: Int): Fragment {
        return data[position]
    }

    override fun getCount(): Int {
        return total
    }
}