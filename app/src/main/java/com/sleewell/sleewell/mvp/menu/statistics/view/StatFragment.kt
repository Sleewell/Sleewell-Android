package com.sleewell.sleewell.mvp.menu.statistics.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AACrosshair
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATooltip
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.menu.statistics.presenter.StatisticsPresenter
import com.sleewell.sleewell.mvp.menu.statistics.model.AnalyseValueStatistic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StatFragment : Fragment(), StatisticsContract.View {

    private var scopeMainThread = CoroutineScope(Job() + Dispatchers.Main)
    private lateinit var presenter: StatisticsContract.Presenter
    private lateinit var root: View

    // Chart
    private lateinit var aaChartModel: AAChartModel

    //widgets
    private lateinit var aaChartView: AAChartView
    private lateinit var textView: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.new_fragment_stat, container, false)
        setPresenter(StatisticsPresenter(root.context as AppCompatActivity, this))
        initWidgets()
        presenter.refreshAnalyse()
        return root
    }

    /**
     * Initialise the widget of the layout
     *
     * @author Hugo Berthomé
     */
    private fun initWidgets() {
        aaChartView = root.findViewById(R.id.AAChartView)
        textView = root.findViewById(R.id.textView)
        loadingProgressBar = root.findViewById(R.id.progressBar)
        errorIcon = root.findViewById(R.id.imageView)

        loadingProgressBar.visibility = View.VISIBLE
        errorIcon.visibility = View.INVISIBLE
    }

    /**
     * Update the graph from the analyse data
     *
     * @param datas from the analyse
     * @author Hugo Berthomé
     */
    override fun displayAnalyse(datas: Array<AnalyseValueStatistic>) {
        aaChartModel = AAChartModel()
            .chartType(AAChartType.Areaspline)
            .axesTextColor("#8a9198")
            .backgroundColor("none")
            .legendEnabled(false)
            .dataLabelsEnabled(false)
            .colorsTheme(arrayOf(AAGradientColor.linearGradient("#04141c", "#8a9198")))
            .markerRadius(0f)
            .gradientColorEnable(true)
            .yAxisMax(55f)
            .yAxisGridLineWidth(0f)
            .yAxisTitle("dB")
            .zoomType(AAChartZoomType.X)
            .animationType(AAChartAnimationType.EaseInOutSine)
            .animationDuration(1000)
            .categories(
                Array(datas.size) { i -> datas[i].ts }
            )
            .series(
                arrayOf(
                    AASeriesElement().data(
                        Array(datas.size) { i -> datas[i].db }
                    )
                )
            )

        val toolTips = AATooltip()
            .useHTML(true)
            .formatter(
                """
function () {
        return ' Sound detected around <b> '
        +  this.x
        + ' </b> of <b>'
        +  this.y.toFixed(2)
        + '</b> dB';
        }
             """.trimIndent()
            )
            .valueDecimals(2)
            .backgroundColor("#000000")
            .borderColor("#000000")
            .style(
                AAStyle()
                    .color("#8a9198")
                    .fontSize(12f)
            )
        val options = aaChartModel.aa_toAAOptions()

        if (options.xAxis != null) {
            options.xAxis!!
                .lineColor("none")
                .crosshair(
                    AACrosshair()
                        .color("none")
                )
        }
        options.yAxis?.gridLineWidth(0f)

        options.tooltip = toolTips
        scopeMainThread.launch {
            aaChartView.aa_drawChartWithChartOptions(options)
            loadingProgressBar.visibility = View.INVISIBLE
        }
    }

    /**
     * Display the date and time of the analyse
     *
     * @param date to display
     * @author Hugo Berthomé
     */
    override fun displayAnalyseDate(date: String) {
        scopeMainThread.launch {
            textView.text = date
        }
    }

    /**
     * Display a message saying no analyse and an error icon
     *
     * @author Hugo Berthomé
     */
    override fun noAnalyseFound() {
        scopeMainThread.launch {
            textView.text = "No analyse found"
            errorIcon.visibility = View.VISIBLE
            loadingProgressBar.visibility = View.GONE
        }
    }

    /**
     * Display an error message
     *
     * @param msg to display
     * @author Hugo Berthomé
     */
    override fun onError(msg: String) {
        scopeMainThread.launch {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            loadingProgressBar.visibility = View.GONE
            errorIcon.visibility = View.VISIBLE
        }
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
}