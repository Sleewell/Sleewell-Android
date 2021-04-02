package com.sleewell.sleewell.mvp.menu.statistics.view

import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseFileUtils
import com.sleewell.sleewell.mvp.menu.statistics.State
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.menu.statistics.presenter.StatisticsPresenter
import com.sleewell.sleewell.mvp.menu.statistics.model.AnalyseValueStatistic
import com.sleewell.sleewell.mvp.menu.statistics.model.dataClass.AnalyseDetail
import com.sleewell.sleewell.mvp.menu.statistics.view.recyclerView.AnalyseRecyclerAdapter
import kotlinx.android.synthetic.main.new_fragment_alarm.*
import kotlinx.android.synthetic.main.new_fragment_stat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class StatFragment : Fragment(), StatisticsContract.View {
    private lateinit var presenter: StatisticsContract.Presenter
    private lateinit var root: View

    // Date widget
    private val FORMAT_DAY = "dd LLLL"
    private val FORMAT_WEEK = "dd LLLL"
    private val FORMAT_MONTH = "LLLL yyyy"
    private val FORMAT_YEAR = "yyyy"
    private val calendar = Calendar.getInstance()
    private lateinit var textDate: TextView
    private lateinit var previousDate: ImageButton
    private lateinit var nextDate: ImageButton

    // Chart
    private lateinit var aaChartModel: AAChartModel
    private var scopeMainThread = CoroutineScope(Job() + Dispatchers.Main)

    //widgets graph
    private lateinit var aaChartView: AAChartView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorIcon: ImageView

    //widgets nav
    private lateinit var dayToggleButton: ToggleButton
    private lateinit var weekToggleButton: ToggleButton
    private lateinit var monthToggleButton: ToggleButton
    private lateinit var yearToggleButton: ToggleButton

    //widgets card Stats
    private lateinit var statCard: CardView
    private lateinit var statTimeSlept: TextView
    private lateinit var statTimeSleeping: TextView
    private lateinit var statTimeWakingUp: TextView

    //widgets card analyse
    private lateinit var analyseCard: CardView
    private lateinit var detailRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.new_fragment_stat, container, false)
        setPresenter(StatisticsPresenter(root.context as AppCompatActivity, this))
        initWidgets()
        initRecyclerView()

        presenter.refreshAnalyse(calendar.time)

        //TODO exemple de detail analyse qui sera envoyé directement du présenteur
        /*val detailList = ArrayList<AnalyseDetail>()
        val test = Icon.createWithResource(context, R.drawable.ic_moon)
        val test2 = Icon.createWithResource(context, R.drawable.ic_smiley_sleep)*/

        return root
    }

    /**
     * Initialise the widget of the layout
     *
     * @author Hugo Berthomé
     */
    private fun initWidgets() {
        textDate = root.findViewById(R.id.textDate)
        previousDate = root.findViewById(R.id.buttonPrev)
        nextDate = root.findViewById(R.id.buttonNext)

        aaChartView = root.findViewById(R.id.AAChartView)
        loadingProgressBar = root.findViewById(R.id.progressBar)
        errorIcon = root.findViewById(R.id.imageView)
        dayToggleButton = root.findViewById(R.id.day_nav)
        weekToggleButton = root.findViewById(R.id.week_nav)
        monthToggleButton = root.findViewById(R.id.month_nav)
        yearToggleButton = root.findViewById(R.id.year_nav)

        statCard = root.findViewById(R.id.StatisticsCard)
        statTimeSlept = root.findViewById(R.id.textTimeSlept)
        statTimeSleeping = root.findViewById(R.id.textStatsTimeSleeping)
        statTimeWakingUp = root.findViewById(R.id.textStatsTimeWakingUp)

        analyseCard = root.findViewById(R.id.AnalyseCard)
        detailRecyclerView = root.findViewById(R.id.StatsRecyclerView)

        //date
        refreshDateView()
        previousDate.setOnClickListener {
            when (presenter.getCurrentState()) {
                State.DAY -> {
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                }
                State.WEEK -> {
                    calendar.add(Calendar.WEEK_OF_YEAR, -1)
                }
                State.MONTH -> {
                    calendar.add(Calendar.MONTH, -1)
                }
                State.YEAR -> {
                    calendar.add(Calendar.YEAR, -1)
                }
            }
            refreshDateView()
        }
        nextDate.setOnClickListener {
            when (presenter.getCurrentState()) {
                State.DAY -> {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                State.WEEK -> {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1)
                }
                State.MONTH -> {
                    calendar.add(Calendar.MONTH, 1)
                }
                State.YEAR -> {
                    calendar.add(Calendar.YEAR, 1)
                }
            }
            refreshDateView()
        }

        toggleOffAll()
        dayToggleButton.setOnClickListener {
            toggleOffAll()
            dayToggleButton.isChecked = true
            setInLoadingState()
            presenter.setCurrentState(State.DAY)
            calendar.time = Date.from(Instant.now())
            refreshDateView()
            presenter.refreshAnalyse(calendar.time)
        }
        weekToggleButton.setOnClickListener {
            toggleOffAll()
            weekToggleButton.isChecked = true
            setInLoadingState()
            presenter.setCurrentState(State.WEEK)
            calendar.time = Date.from(Instant.now())
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            refreshDateView()
            presenter.refreshAnalyse(calendar.time)
        }
        monthToggleButton.setOnClickListener {
            toggleOffAll()
            monthToggleButton.isChecked = true
            setInLoadingState()
            presenter.setCurrentState(State.MONTH)
            calendar.time = Date.from(Instant.now())
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            refreshDateView()
            presenter.refreshAnalyse(calendar.time)
        }
        yearToggleButton.setOnClickListener {
            toggleOffAll()
            yearToggleButton.isChecked = true
            setInLoadingState()
            presenter.setCurrentState(State.YEAR)
            calendar.time = Date.from(Instant.now())
            calendar.set(Calendar.DAY_OF_YEAR, 1)
            refreshDateView()
            presenter.refreshAnalyse(calendar.time)
        }
        when (presenter.getCurrentState()) {
            State.DAY -> dayToggleButton.isChecked = true
            State.WEEK -> weekToggleButton.isChecked = true
            State.MONTH -> monthToggleButton.isChecked = true
            State.YEAR -> yearToggleButton.isChecked = true
        }

        errorIcon.visibility = View.INVISIBLE
        setInLoadingState()
    }

    private fun initRecyclerView() {
        detailRecyclerView.isNestedScrollingEnabled = false
        detailRecyclerView.layoutManager = LinearLayoutManager(root.context.applicationContext)
        detailRecyclerView.itemAnimator = DefaultItemAnimator()
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
            textDate.text = date
        }
    }

    /**
     * Display a message saying no analyse and an error icon
     *
     * @author Hugo Berthomé
     */
    override fun noAnalyseFound() {
        scopeMainThread.launch {
            textDate.text = "No analyse found"
            errorIcon.visibility = View.VISIBLE
            loadingProgressBar.visibility = View.GONE
            hideAllCards()
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
            hideAllCards()
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
        scopeMainThread.launch {
            statCard.visibility = View.VISIBLE
            statTimeSlept.text = timestampToDuration(timeSleeping)
            statTimeSleeping.text = timestampToDuration(timeGoingToSleep)
            statTimeWakingUp.text = timestampToDuration(timeWakingUp)
        }
    }

    /**
     * Display night analyse advices to the use
     *
     * @param advices List of the advices to display (if list empty ward will be gone)
     * @author Hugo Berthomé
     */
    override fun displayAnalyseAdvices(advices: ArrayList<AnalyseDetail>) {
        scopeMainThread.launch {
            if (advices.size == 0) {
                analyseCard.visibility = View.GONE
            } else {
                val adapter = AnalyseRecyclerAdapter(advices)
                detailRecyclerView.adapter = adapter
                analyseCard.visibility = View.VISIBLE
            }
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

    /**
     * Toggle nav switch stats to off
     *
     * @author Hugo Berthomé
     */
    private fun toggleOffAll() {
        dayToggleButton.isChecked = false
        weekToggleButton.isChecked = false
        monthToggleButton.isChecked = false
        yearToggleButton.isChecked = false
    }

    /**
     * Hide all the cards from the view
     *
     * @author Hugo Berthomé
     */
    private fun hideAllCards() {
        analyseCard.visibility = View.GONE
        statCard.visibility = View.GONE
    }

    /**
     * Set the view in loading state
     * Hide the graph and the cards and show a loading spinner
     *
     * @author Hugo Berthomé
     */
    private fun setInLoadingState() {
        hideAllCards()
        aaChartView.visibility = View.INVISIBLE
        errorIcon.visibility = View.INVISIBLE
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun timestampToDuration(timestamp: Long): String {
        var duration = DateUtils.formatElapsedTime(timestamp)
        if (duration.length == 5) {
            duration = "0:$duration"
        }
        return duration.substring(IntRange(0, duration.length - 4))
        /*return DateTimeFormatter
            .ofPattern(AudioAnalyseFileUtils.DATE_FORMAT)
            .withZone(ZoneOffset.systemDefault())
            .format(Instant.ofEpochSecond(timestamp))*/
    }

    private fun refreshDateView() {
        if (presenter.getCurrentState() == State.WEEK) {
            var weekDate = dateToString(calendar.time) + " - "
            calendar.add(Calendar.DAY_OF_MONTH, 6)
            weekDate += dateToString(calendar.time)
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            textDate.text = weekDate
        } else {
            textDate.text = dateToString(calendar.time)
        }
    }

    private fun dateToString(date: Date): String {
        val dateFormat = when (presenter.getCurrentState()) {
            State.DAY -> FORMAT_DAY
            State.WEEK -> FORMAT_WEEK
            State.MONTH -> FORMAT_MONTH
            State.YEAR -> FORMAT_YEAR
            else -> {
                FORMAT_DAY
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return DateTimeFormatter
            .ofPattern(dateFormat)
            .withZone(ZoneOffset.systemDefault())
            .format(date.toInstant())
        }
        return SimpleDateFormat(dateFormat).format(date)
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