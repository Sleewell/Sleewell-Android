package com.sleewell.sleewell.mvp.menu.statistics.view

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.menu.statistics.presenter.StatisticsPresenter
import com.sleewell.sleewell.mvp.menu.statistics.model.AnalyseValueStatistic
import com.sleewell.sleewell.mvp.menu.statistics.model.dataClass.AnalyseDetail
import com.sleewell.sleewell.mvp.menu.statistics.view.recyclerView.AnalyseRecyclerAdapter
import kotlinx.android.synthetic.main.new_fragment_alarm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StatFragment : Fragment(), StatisticsContract.View {

    private lateinit var presenter: StatisticsContract.Presenter
    private lateinit var root: View

    // Chart
    private lateinit var aaChartModel: AAChartModel
    private var scopeMainThread = CoroutineScope(Job() + Dispatchers.Main)

    //widgets
    private lateinit var aaChartView: AAChartView
    private lateinit var textView: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorIcon: ImageView
    private lateinit var detailRecyclerView: RecyclerView

    //recyclerView
    private val detailList = ArrayList<AnalyseDetail>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.new_fragment_stat, container, false)
        setPresenter(StatisticsPresenter(root.context as AppCompatActivity, this))
        initWidgets()
        presenter.refreshAnalyse()

        val test = Icon.createWithResource(context, R.drawable.ic_moon)
        val test2 = Icon.createWithResource(context, R.drawable.ic_smiley_sleep)
        detailList.add(AnalyseDetail(test, "ceci est le premier message"))
        detailList.add(AnalyseDetail(test2, "ceci est le 2 eme message"))

        initRecyclerView()
        return root
    }

    /**
     * Initialise the widget of the layout
     *
     * @author Hugo Berthomé
     */
    private fun initWidgets() {
        aaChartView = root.findViewById(R.id.AAChartView)
        textView = root.findViewById(R.id.textDate)
        loadingProgressBar = root.findViewById(R.id.progressBar)
        errorIcon = root.findViewById(R.id.imageView)
        detailRecyclerView = root.findViewById(R.id.StatsRecyclerView)

        loadingProgressBar.visibility = View.VISIBLE
        errorIcon.visibility = View.INVISIBLE
    }

    private fun initRecyclerView() {
        val adapter = AnalyseRecyclerAdapter(detailList)

        detailRecyclerView.isNestedScrollingEnabled = false
        detailRecyclerView.layoutManager = LinearLayoutManager(root.context.applicationContext)
        detailRecyclerView.itemAnimator = DefaultItemAnimator()
        detailRecyclerView.adapter = adapter
    }

    /**
     * Update the graph for the daily analyse
     *
     * TODO params
     * @author Hugo Berthomé
     */
    override fun displayAnalyseDay() {
        TODO("Not yet implemented")
    }

    /**
     * Update the graph for the Weekly analyse
     *
     * TODO params
     * @author Hugo Berthomé
     */
    override fun displayAnalyseWeek() {
        TODO("Not yet implemented")
    }

    /**
     * Update the graph for the Month analyse
     *
     * TODO params
     * @author Hugo Berthomé
     */
    override fun displayAnalyseMonth() {
        TODO("Not yet implemented")
    }

    /**
     * Update the graph for the year analyse
     *
     * TODO params
     * @author Hugo Berthomé
     */
    override fun displayAnalyseYear() {
        TODO("Not yet implemented")
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
     * Display the night information
     *
     * @param timeSleeping total sleeping time
     * @param timeGoingToSleep time when going to sleep
     * @param timeWakingUp time when waking up
     * @author Hugo Berthomé
     */
    override fun displayNightData(timeSleeping: Long, timeGoingToSleep: Long, timeWakingUp: Long) {
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

    // INFO will disappear
    override fun displayAnalyse(datas: Array<AnalyseValueStatistic>) {
        /*aaChartModel = AAChartModel()
            .chartType(AAChartType.Areaspline)
            .axesTextColor("#8a9198")
            .backgroundColor("none")
            .legendEnabled(false)
            .dataLabelsEnabled(false)
            .colorsTheme(arrayOf(AAGradientColor.linearGradient("#04141c", "#8a9198")))
            .markerRadius(0f)
            .gradientColorEnable(true)
            .yAxisMin(10f)
            .yAxisMax(datas.maxOf { it.db }.toFloat())
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
        }*/
    }
}