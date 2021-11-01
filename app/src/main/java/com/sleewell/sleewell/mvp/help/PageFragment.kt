package com.sleewell.sleewell.mvp.help

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            1 -> R.layout.fragment_page_home
            2 -> R.layout.fragment_page_alarm
            else -> R.layout.fragment_page_home
        }
        return inflater.inflate(layoutId, container,  false)
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
    }
}