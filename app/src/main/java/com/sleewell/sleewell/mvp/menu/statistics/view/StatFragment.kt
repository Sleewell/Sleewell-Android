package com.sleewell.sleewell.mvp.menu.statistics.view

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
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AACrosshair
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATooltip
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import com.sleewell.sleewell.R
import com.sleewell.sleewell.api.sleewell.model.NightAnalyse
import com.sleewell.sleewell.mvp.menu.statistics.State
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.menu.statistics.presenter.StatisticsPresenter
import com.sleewell.sleewell.mvp.menu.statistics.model.StatisticsModel
import com.sleewell.sleewell.mvp.menu.statistics.model.dataClass.AnalyseDetail
import com.sleewell.sleewell.mvp.menu.statistics.view.recyclerView.AnalyseRecyclerAdapter
import kotlinx.android.synthetic.main.new_fragment_alarm.*
import kotlinx.android.synthetic.main.new_fragment_stat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class StatFragment : Fragment(), StatisticsContract.View {
    private lateinit var presenter: StatisticsContract.Presenter
    private lateinit var root: View

    // widget date
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
    private lateinit var toolTipBarChar: AATooltip
    private lateinit var toolTipGraphChar: AATooltip
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

    //widget msg
    private lateinit var textMsg: TextView

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

        textMsg = root.findViewById(R.id.textMessage)

        //date
        calendar.add(Calendar.DAY_OF_YEAR, -1)
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
            setInLoadingState()
            presenter.refreshAnalyse(calendar.time)
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
            setInLoadingState()
            presenter.refreshAnalyse(calendar.time)
        }

        toggleOffAll()
        dayToggleButton.setOnClickListener {
            toggleOffAll()
            dayToggleButton.isChecked = true
            setInLoadingState()
            presenter.setCurrentState(State.DAY)
            calendar.time = Date.from(Instant.now())
            calendar.add(Calendar.DAY_OF_YEAR, -1)
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
        initToolTipBarChart()
        initToolTipGraphChart()
    }

    private fun initRecyclerView() {
        detailRecyclerView.isNestedScrollingEnabled = false
        detailRecyclerView.layoutManager = LinearLayoutManager(root.context.applicationContext)
        detailRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    /**
     * Update the graph for the daily analyse
     *
     * @param data from the night analyse
     * @author Hugo Berthomé
     */
    override fun displayAnalyseDay(data: NightAnalyse) {
        if (data.data == null)
            return
        errorIcon.visibility = View.INVISIBLE
        aaChartModel = AAChartModel()
            .chartType(AAChartType.Areaspline)
            .axesTextColor("#8a9198")
            .backgroundColor("none")
            .legendEnabled(false)
            .dataLabelsEnabled(false)
            .colorsTheme(arrayOf(AAGradientColor.linearGradient("#04141c", "#8a9198")))
            .markerRadius(0f)
            .gradientColorEnable(true)
            .yAxisMin(10f)
            .yAxisMax(data.data.maxOf { it.db }.toFloat())
            .yAxisGridLineWidth(0f)
            .yAxisTitle("dB")
            .yAxisVisible(false)
            .xAxisVisible(false)
            .zoomType(AAChartZoomType.X)
            .animationType(AAChartAnimationType.EaseInOutSine)
            .animationDuration(1000)
            .categories(
                Array(data.data.size) { i -> timestampToDateString(data.data[i].ts) }
            )
            .series(
                arrayOf(
                    AASeriesElement().data(
                        Array(data.data.size) { i -> data.data[i].db }
                    )
                )
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

        options.tooltip = toolTipGraphChar
        scopeMainThread.launch {
            aaChartView.aa_drawChartWithChartOptions(options)
            loadingProgressBar.visibility = View.INVISIBLE
            aaChartView.visibility = View.VISIBLE
        }
    }

    /**
     * Update the graph for the Weekly analyse
     *
     * @param datas list of data from the nights analyse
     * @author Hugo Berthomé
     */
    override fun displayAnalyseWeek(datas: Array<NightAnalyse>) {
        createBarChartModel(arrayListOf("Mon", "Tue", "Wed", "Thu", "Fry", "Sat", "Sun"), datas)
        val options = aaChartModel.aa_toAAOptions()

        errorIcon.visibility = View.INVISIBLE
        if (options.xAxis != null) {
            options.xAxis!!
                .lineColor("none")
                .crosshair(
                    AACrosshair()
                        .color("none")
                )
        }

        options.yAxis?.gridLineWidth(0f)
        options.tooltip = toolTipBarChar
        scopeMainThread.launch {
            aaChartView.aa_drawChartWithChartOptions(options)
            loadingProgressBar.visibility = View.INVISIBLE
            aaChartView.visibility = View.VISIBLE
        }
    }

    /**
     * Update the graph for the Month analyse
     *
     * @param datas list of data from the nights analyse
     * @author Hugo Berthomé
     */
    override fun displayAnalyseMonth(datas: Array<NightAnalyse>) {
        val calendarWeek = Calendar.getInstance()
        calendarWeek.time = calendar.time
        calendarWeek.set(Calendar.WEEK_OF_MONTH, 1)
        val max = calendarWeek.getActualMaximum(Calendar.WEEK_OF_MONTH)

        errorIcon.visibility = View.INVISIBLE
        val categories = ArrayList<String>()
        for (i in 1..max) {
            categories.add("Week $i")
        }
        createBarChartModel(
            categories, datas
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
        options.tooltip = toolTipBarChar
        scopeMainThread.launch {
            aaChartView.aa_drawChartWithChartOptions(options)
            loadingProgressBar.visibility = View.INVISIBLE
            aaChartView.visibility = View.VISIBLE
        }
    }

    /**
     * Update the graph for the year analyse
     *
     * @param datas list of data from the nights analyse
     * @author Hugo Berthomé
     */
    override fun displayAnalyseYear(datas: Array<NightAnalyse>) {
        createBarChartModel(
            arrayListOf(
                "Jan",
                "Feb",
                "Mar",
                "Avp",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
            ), datas
        )
        val options = aaChartModel.aa_toAAOptions()

        errorIcon.visibility = View.INVISIBLE
        if (options.xAxis != null) {
            options.xAxis!!
                .lineColor("none")
                .crosshair(
                    AACrosshair()
                        .color("none")
                )
        }

        options.yAxis?.gridLineWidth(0f)
        options.tooltip = toolTipBarChar
        scopeMainThread.launch {
            aaChartView.aa_drawChartWithChartOptions(options)
            loadingProgressBar.visibility = View.INVISIBLE
            aaChartView.visibility = View.VISIBLE
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
            textMsg.text = "No analyse found"
            textMsg.visibility = View.VISIBLE
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
            /*Toast.makeText(context, msg, Toast.LENGTH_LONG).show()*/
            loadingProgressBar.visibility = View.GONE
            errorIcon.visibility = View.VISIBLE
            aaChartView.visibility = View.INVISIBLE
            textMsg.text = msg
            textMsg.visibility = View.VISIBLE
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
            if (timeGoingToSleep == 0L && timeSleeping == 0L && timeWakingUp == 0L) {
                hideAllCards()
                return@launch
            }
            statCard.visibility = View.VISIBLE
            statTimeSlept.text = timestampToDuration(timeSleeping)
            statTimeSleeping.text = timestampToDateString(timeGoingToSleep)
            if (timeWakingUp >= 24 * 60 * 60) {
                statTimeWakingUp.text = timestampToDateString(timeWakingUp - (24 * 60 * 60))
            } else {
                statTimeWakingUp.text = timestampToDateString(timeWakingUp)
            }
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
        textMsg.visibility = View.GONE
        aaChartView.visibility = View.INVISIBLE
        errorIcon.visibility = View.INVISIBLE
        loadingProgressBar.visibility = View.VISIBLE
    }

    /**
     * Refresh the date on the view
     *
     * @author Hugo Berthomé
     */
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

    /**
     * Creates a Bar chart model from data
     *
     * @param categories To display
     * @param nights Data of each categories
     * @author Hugo Berthomé
     */
    private fun createBarChartModel(categories: ArrayList<String>, nights: Array<NightAnalyse>) {
        var filteredNights = nights.filterIndexed { index, nightAnalyse ->
            !(index < nights.size - 1 && nightAnalyse.id == nights[index + 1].id)
        }.toTypedArray()
        filteredNights = fillArrayWithNoValues(filteredNights)
        aaChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .axesTextColor("#8a9198")
            .backgroundColor("none")
            .legendEnabled(false)
            .dataLabelsEnabled(false)
            .colorsTheme(arrayOf(AAGradientColor.linearGradient("#04141c", "#8a9198")))
            .markerRadius(0f)
            .gradientColorEnable(true)
            .yAxisMax(57600f)
            .yAxisGridLineWidth(0f)
            .yAxisVisible(false)
            .animationType(AAChartAnimationType.EaseInOutSine)
            .animationDuration(1000)
            .categories(
                categories.toTypedArray()
            )
            .series(
                arrayOf(
                    AASeriesElement().data(
                        Array(categories.size) { i ->
                            if (i >= filteredNights.size)
                                return@Array 0.0
                            return@Array filteredNights[i].end.toDouble() - filteredNights[i].start.toDouble()
                        }
                    )
                )
            )
    }

    private fun fillArrayWithNoValues(listData: Array<NightAnalyse>): Array<NightAnalyse> {
        val tmpCalendar = Calendar.getInstance()
        tmpCalendar.time = calendar.time
        var indexListData = 0

        when (presenter.getCurrentState()) {
            State.DAY -> {
                return listData
            }
            State.WEEK -> {
                return Array(7) { i ->
                    if (i != 0)
                        tmpCalendar.add(Calendar.DAY_OF_YEAR, 1)

                    val id = dateToString(tmpCalendar.time, StatisticsModel.FORMAT_DAY)
                    if (indexListData < listData.size && listData[indexListData].id == id) {
                        indexListData++
                        return@Array listData[indexListData - 1]
                    }
                    return@Array NightAnalyse(null, 0, null, 0, id)
                }
            }
            State.MONTH -> {
                return listData
            }
            State.YEAR -> {
                return listData
            }
        }
    }

    /**
     * Initialise tool tip for the Bar chart
     * Data overview indication
     *
     * @author Hugo Berthomé
     */
    private fun initToolTipBarChart() {
        toolTipBarChar = AATooltip()
            .useHTML(true)
            .formatter(
                """
                function () {
                    var hours   = Math.floor(this.y / 3600);
                    var minutes = Math.floor((this.y - (hours * 3600)) / 60);
        
                    var hoursTxt = "0";
                    var minTxt = "0";
        
                    if (hours   < 10) {hours   = '0' + hours;}
                    if (minutes < 10) {minutes = '0' + minutes;}
        
                    return '<b> '
                            +  hours
                            +  ':'
                            +  minutes
                            + '</b>';
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
    }

    /**
     * Initialise tool tip for the graph chart
     * Data overview indication
     *
     * @author Hugo Berthomé
     */
    private fun initToolTipGraphChart() {
        toolTipGraphChar = AATooltip()
            .useHTML(true)
            .formatter(
                """
                function () {
                    return '<b> '
                            +  this.y.toFixed(0)
                            + ' </b> dB around <b>'
                            +  this.x
                            + '</b>';
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
    }

    /**
     * Converts a date to string
     *
     * @param date to convert
     * @return String representing the date
     */
    private fun dateToString(date: Date, state: State = presenter.getCurrentState()): String {
        val dateFormat = when (state) {
            State.DAY -> FORMAT_DAY
            State.WEEK -> FORMAT_WEEK
            State.MONTH -> FORMAT_MONTH
            State.YEAR -> FORMAT_YEAR
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return DateTimeFormatter
                .ofPattern(dateFormat)
                .withZone(ZoneOffset.systemDefault())
                .format(date.toInstant())
        }
        return SimpleDateFormat(dateFormat, Locale.FRANCE).format(date)
    }

    private fun dateToString(date: Date, dateFormat: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return DateTimeFormatter
                .ofPattern(dateFormat)
                .withZone(ZoneOffset.systemDefault())
                .format(date.toInstant())
        }
        return SimpleDateFormat(dateFormat, Locale.FRANCE).format(date)
    }

    /**
     * Converts a timestamp into a String representation
     *
     * @param timestamp
     * @param dateFormat Format of the string at the end
     * @return String representing the date
     */
    private fun timestampToDateString(timestamp: Long, dateFormat: String = "HH:mm"): String {
        return DateTimeFormatter
            .ofPattern(dateFormat)
            .withZone(ZoneOffset.systemDefault())
            .format(Instant.ofEpochSecond(timestamp))
    }

    /**
     * Converts a timestamp to duration String
     *
     * @param timestamp
     * @return String representing the duration in HH:mm
     */
    private fun timestampToDuration(timestamp: Long): String {
        var duration = DateUtils.formatElapsedTime(timestamp)
        if (duration.length == 5) {
            duration = "0:$duration"
        }
        return duration.substring(IntRange(0, duration.length - 4))
    }
}