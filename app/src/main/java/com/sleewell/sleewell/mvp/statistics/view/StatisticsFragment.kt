package com.sleewell.sleewell.mvp.statistics.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.aachartmodel.aainfographics.AAInfographicsLib.AAChartCreator.*
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.statistics.presenter.StatisticsPresenter

class StatisticsFragment : Fragment(), StatisticsContract.View {
    private lateinit var presenter: StatisticsContract.Presenter
    private lateinit var root: View

    //View widgets
    private lateinit var btnStart: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_statistics, container, false)

        setPresenter(StatisticsPresenter(this, root.context as AppCompatActivity))
        initWidget()
        return root
    }

    override fun displayToast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Set the presenter inside the class
     *
     * @param presenter
     * @author Hugo Berthomé
     */
    override fun setPresenter(presenter: StatisticsContract.Presenter) {
        this.presenter = presenter
    }

    private fun initWidget() {
        //get widgets
        btnStart = root.findViewById(R.id.btn_start)

        //init event listener
        btnStart.setOnClickListener {
            presenter.onStartClick()
        }
    }
}