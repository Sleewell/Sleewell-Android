package com.example.sleewell.nav.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sleewell.R

class StatisticsFragment : Fragment() {

    private lateinit var notificationsViewModel: StatisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(StatisticsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_statistics, container, false)
        val textView: TextView = root.findViewById(R.id.text_statistics)
        notificationsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}