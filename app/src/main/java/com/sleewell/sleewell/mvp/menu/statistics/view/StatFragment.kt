package com.sleewell.sleewell.mvp.menu.statistics.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATooltip
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatFragment : Fragment(), StatisticsContract.View {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var presenter: StatisticsContract.Presenter
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.new_fragment_stat, container, false)

        val aaChartView: AAChartView = root.findViewById(R.id.AAChartView)
        /*aaChartView.background?.alpha = 0*/
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Areaspline)
            .axesTextColor("#8a9198")
            .backgroundColor("none")
            .legendEnabled(false)
            .dataLabelsEnabled(false)
            .colorsTheme(arrayOf(AAGradientColor.linearGradient("#04141c", "#8a9198")))
            .markerRadius(0f)
            .stacking(AAChartStackingType.Normal)
            .gradientColorEnable(true)
            .yAxisGridLineWidth(0f)
            .categories(
                arrayOf(
                    "20:00",
                    "7:00"
                )
            ) // TODO mettre l'heure que l'on récupèrera des datas au format : HH-MM-SS
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Tokyo")
                        .data(
                            arrayOf(
                                0.0,
                                6.9,
                                9.5,
                                14.5,
                                18.2,
                                21.5,
                                25.2,
                                26.5,
                                23.3,
                                18.3,
                                13.9,
                                0.0
                            )
                        )
                )
            )

        //val option = aaChartModel.aa_toAAOptions()
        val toolTips = AATooltip()
            .useHTML(true)
            .formatter(
                """
function () {
        return ' 🌕 🌖 🌗 🌘 🌑 🌒 🌓 🌔 <br/> '
        + ' Support JavaScript Function Just Right Now !!! <br/> '
        + ' The Gold Price For <b>2020 '
        +  this.x
        + ' </b> Is <b> '
        +  this.y
        + ' </b> Dollars ';
        }
             """.trimIndent()
            )
            .valueDecimals(2)//设置取值精确到小数点后几位//设置取值精确到小数点后几位
            .backgroundColor("#000000")
            .borderColor("#000000")
            .style(
                AAStyle()
                    .color("#FFD700")
                    .fontSize(12f)
            )
        val options = aaChartModel.aa_toAAOptions()
        options.tooltip = toolTips
        aaChartView.aa_drawChartWithChartOptions(options)

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun displayAnalyse(datas: Array<AnalyseValue>) {
        TODO("Not yet implemented")
    }

    override fun noAnalyseFound() {
        TODO("Not yet implemented")
    }

    override fun onError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
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