package com.sleewell.sleewell.mvp.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R

private const val POSITION = "POSITION"

class PageFragment : Fragment() {

    var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = when (position) {
            1 -> R.layout.fragment_page_start
            2 -> R.layout.fragment_page_home
            3 -> R.layout.fragment_page_alarm
            4 -> R.layout.fragment_page_routine
            5 -> R.layout.fragment_page_stat
            else -> R.layout.fragment_page_end
        }
        return inflater.inflate(layoutId, container,  false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val skip = view.findViewById<Button>(R.id.skipButton)
        skip.setOnClickListener {
            (activity as OnBoardingActivity).dismissActivity()
        }

    }


}