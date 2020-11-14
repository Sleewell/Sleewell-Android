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
import com.sleewell.sleewell.modules.audioRecord.SoundDataUtils
import com.sleewell.sleewell.mvp.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.statistics.presenter.StatisticsPresenter
import java.util.*
import kotlin.math.sqrt
import java.lang.Math as Math1

class StatisticsFragment : Fragment(), StatisticsContract.View {
    private lateinit var presenter: StatisticsContract.Presenter
    private lateinit var root: View

    //View widgets
    private lateinit var btnStart: Button
    private lateinit var aaChartView: AAChartView

    //Graph model
    private lateinit var chartModel: AAChartModel
    private val limitData = 300
    private var arrayData: Queue<Double> = LinkedList<Double>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_statistics, container, false)

        setPresenter(StatisticsPresenter(this, root.context as AppCompatActivity))
        initWidget()
        initGraph()
        return root
    }

    override fun displayToast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }

    override fun updateGraph(array: ShortArray) {

        val valueToAdd = SoundDataUtils.calculateMean(array)

        arrayData.add(valueToAdd)
        if (arrayData.size >= limitData)
            arrayData.remove()

        this.aaChartView.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(arrayOf(
            AASeriesElement()
                .name("PMC")
                .data(arrayData.toTypedArray())
        ), false)
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
        aaChartView = root.findViewById(R.id.AAChartView_sound)

        //init event listener
        btnStart.setOnClickListener {
            presenter.onStartClick()
        }
    }

    private fun initGraph() {
        for (i in 0 until limitData)
            arrayData.add(0.0)
        chartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .backgroundColor("#FFFFFF")
            .yAxisMax(5000f)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("PMC")
                        .data(arrayData.toTypedArray())
                )
            )
        this.aaChartView.aa_drawChartWithChartModel(chartModel)
    }
}