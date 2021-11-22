 package com.sleewell.sleewell.mvp.music.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.music.MainContract
import com.sleewell.sleewell.mvp.music.presenter.MusicPresenter
import es.claucookie.miniequalizerlibrary.EqualizerView
import java.lang.ClassCastException

 /**
 * A simple [Fragment] subclass.
 * Use the [MusicFragment] factory method to
 * create an instance of this fragment.
 */
class MusicFragment : MainContract.View, DialogFragment() {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var root: View
    private lateinit var listView: ListView
    private lateinit var buttonForest: Button
    private lateinit var buttonWind: Button
    private lateinit var buttonRain: Button
    private lateinit var buttonWater: Button
    private lateinit var buttonFire: Button

     private lateinit var circleForest: View
     private lateinit var circleWind: View
     private lateinit var circleRain: View
     private lateinit var circleWater: View
     private lateinit var circleFire: View


    private var circle: View? = null
    private var equalizer: EqualizerView? = null
    private lateinit var mainText: TextView
    private var musicSelected: Int = -1

    companion object {
        var music_selected: Boolean = false
        var musicName: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.new_fragment_music, container, false)

        setPresenter(MusicPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()
        initListView()
        initActivityWidgets()

        return root
    }

     interface OnInputSelected {
         fun sendInput(musicName: String, tag: String?)
     }
     lateinit var selected : OnInputSelected

    private fun initListView() {
        listView = root.findViewById(R.id.playlistMusicListView)
        listView.adapter = presenter.getAdapterMusiqueByName("forest")

        circle = root.findViewById(R.id.circle_forest)
        equalizer = root.findViewById(R.id.equalizer_view)

        listView.onItemClickListener = AdapterView.OnItemClickListener{ _, viewItem, i, _ ->

            circle!!.visibility = View.INVISIBLE
            if (equalizer != null && equalizer!!.isAnimating && i != musicSelected) {
                equalizer!!.visibility = View.INVISIBLE
                equalizer!!.stopBars()
            }

            circle = viewItem.findViewById(R.id.circle_forest)
            if (i == -1 || i != musicSelected)
                equalizer = viewItem.findViewById(R.id.equalizer_view)

            circle!!.visibility = View.VISIBLE
            if (equalizer!!.isAnimating) {
                equalizer!!.stopBars()
                equalizer!!.visibility = View.INVISIBLE
                presenter.stopMusique()
            } else {
                equalizer!!.animateBars()
                equalizer!!.visibility = View.VISIBLE
                presenter.launchMusique(i)
            }
            musicSelected = i
            music_selected = true
            val main = mainText.text.toString()
            val title = viewItem.findViewById<TextView>(R.id.soundTitle)!!.text.toString()
            musicName = main + "_" + title
        }
    }

     override fun getTheme(): Int {
         return R.style.DialogTheme
     }

    /**
     * This method  setup every widgets created
     *
     * @author gabin warnier de wailly
     */
    private fun initActivityWidgets() {

        mainText = root.findViewById(R.id.main_image_title)
        buttonFire = root.findViewById(R.id.nav_music_fire)
        buttonWind = root.findViewById(R.id.nav_music_wind)
        buttonWater = root.findViewById(R.id.nav_music_water)
        buttonRain = root.findViewById(R.id.nav_music_rain)
        buttonForest = root.findViewById(R.id.nav_music_forest)
        circleFire = root.findViewById(R.id.circle_fire)
        circleWind = root.findViewById(R.id.circle_wind)
        circleWater = root.findViewById(R.id.circle_water)
        circleRain = root.findViewById(R.id.circle_rain)
        circleForest = root.findViewById(R.id.circle_forest)

        buttonForest.setOnClickListener{
            setCircleInvisible()
            circleForest.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("forest")
            mainText.text = "forest"
        }

        buttonWind.setOnClickListener{
            setCircleInvisible()
            circleWind.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("wind")
            mainText.text = "wind"
        }

        buttonWater.setOnClickListener{
            setCircleInvisible()
            circleWater.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("water")
            mainText.text = "water"
        }

        buttonFire.setOnClickListener{
            setCircleInvisible()
            circleFire.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("fire")
            mainText.text = "fire"
        }

        buttonRain.setOnClickListener{
            setCircleInvisible()
            circleRain.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("rain")
            mainText.text = "rain"
        }

        val menu = root.findViewById<Button>(R.id.MenuButton)
        menu.setOnClickListener {
            selected.sendInput(musicName, tag)
            dismiss()
        }
    }

     override fun onAttach(context: Context) {
         super.onAttach(context)
         try {
             selected = targetFragment as OnInputSelected
         } catch (e : ClassCastException) {
             Log.e("MusicFragment", e.message.toString())
         }
     }

     private fun setCircleInvisible() {
         circleForest.visibility = View.INVISIBLE
         circleWater.visibility = View.INVISIBLE
         circleWind.visibility = View.INVISIBLE
         circleFire.visibility = View.INVISIBLE
         circleRain.visibility = View.INVISIBLE

         circle!!.visibility = View.INVISIBLE
         if (equalizer != null) {
             equalizer!!.stopBars()
             equalizer!!.visibility = View.INVISIBLE
         }
         presenter.stopMusique()
         musicSelected = -1
     }

    /**
     * This method save the presenter in the class
     *
     * @param presenter current presenter
     * @author gabin warnier de wailly
     */
    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    /**
     * This method is the destructor of the class
     *
     * @author gabin warnier de wailly
     */
    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}