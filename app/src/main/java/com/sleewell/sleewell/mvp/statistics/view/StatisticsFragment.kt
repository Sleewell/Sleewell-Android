package com.sleewell.sleewell.mvp.statistics.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aachartmodel.aainfographics.AAInfographicsLib.AAChartCreator.*
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.audio.audioTransformation.SoundDataUtils
import com.sleewell.sleewell.mvp.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.statistics.presenter.StatisticsPresenter
import java.util.*

class StatisticsFragment : Fragment(), StatisticsContract.View {
    private lateinit var presenter: StatisticsContract.Presenter
    private lateinit var root: View

    //View widgets
    private lateinit var btnStart: Button
    private lateinit var aaChartViewAmplitude: AAChartView
    private lateinit var aaChartViewSpec: AAChartView

    //Graph model amplitude
    private lateinit var chartModelAmplitude: AAChartModel
    private val limitDataAmplitude = 300
    private var arrayDataAmplitude: Queue<Double> = LinkedList<Double>()

    //Graph model spectrogram
    private lateinit var chartModelSpec: AAChartModel
    private val limitDataSpec = 100
    private var arrayDataSpec: Queue<DoubleArray> = LinkedList<DoubleArray>()

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

    override fun updateGraphAmplitude(array: ShortArray) {

        val valueToAdd = SoundDataUtils.calculateMean(array)

        arrayDataAmplitude.add(valueToAdd)
        if (arrayDataAmplitude.size >= limitDataAmplitude)
            arrayDataAmplitude.remove()

        this.aaChartViewAmplitude.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(arrayOf(
            AASeriesElement()
                .name("PMC")
                .data(arrayDataAmplitude.toTypedArray())
        ), false)
    }

    override fun updateGraphSpec(magnitude: DoubleArray) {
        TODO("Not yet implemented")
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
        aaChartViewAmplitude = root.findViewById(R.id.AAChartView_sound)
        aaChartViewSpec = root.findViewById(R.id.AAChartView_spect)

        //init event listener
        btnStart.setOnClickListener {
            presenter.onStartClick()
        }
    }

    private fun initGraph() {
        for (i in 0 until limitDataAmplitude)
            arrayDataAmplitude.add(0.0)
        chartModelAmplitude = AAChartModel()
            .chartType(AAChartType.Column)
            .backgroundColor("#FFFFFF")
            .yAxisMax(5000f)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("PMC")
                        .data(arrayDataAmplitude.toTypedArray())
                )
            )
        this.aaChartViewAmplitude.aa_drawChartWithChartModel(chartModelAmplitude)

        for (i in 0 until limitDataSpec)
            arrayDataSpec.add(DoubleArray(200) { 0.0 })
        chartModelSpec = AAChartModel()
            .chartType(AAChartType.Scatter)
            .backgroundColor("#FFFFFF")
            .yAxisMax(5000f)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("-1000")
                        .data(arrayDataSpec.toTypedArray()),
                    AASeriesElement()
                        .name("-5000")
                        .data(arrayOf(
                            arrayOf(
                                0,
                                4000),
                            arrayOf(
                                0,
                                5000)
                        ))
                )
            )
        this.aaChartViewSpec.aa_drawChartWithChartModel(chartModelSpec)
    }

    private fun convertArraySpecForGraph()
    {

    }
}