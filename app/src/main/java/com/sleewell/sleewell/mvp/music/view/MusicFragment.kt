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
 * Use the [MusicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MusicFragment : MainContract.View, DialogFragment() {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var root: View
    private lateinit var listView: ListView
    private lateinit var button_forest: Button
    private lateinit var button_wind: Button
    private lateinit var button_rain: Button
    private lateinit var button_water: Button
    private lateinit var button_fire: Button

     private lateinit var circle_forest: View
     private lateinit var circle_wind: View
     private lateinit var circle_rain: View
     private lateinit var circle_water: View
     private lateinit var circle_fire: View


    private var circle: View? = null
    private var equalizer: EqualizerView? = null
    private lateinit var main_text: TextView
    private var musicSelected: Int = -1

    companion object {
        var music_selected: Boolean = false
        var musicName: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.new_fragment_music, container, false)

        setPresenter(MusicPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()
        initListView()
        InitActivityWidgets()

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
            val main = main_text!!.text.toString()
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
    private fun InitActivityWidgets() {

        main_text = root.findViewById(R.id.main_image_title)
        button_fire = root.findViewById(R.id.nav_music_fire)
        button_wind = root.findViewById(R.id.nav_music_wind)
        button_water = root.findViewById(R.id.nav_music_water)
        button_rain = root.findViewById(R.id.nav_music_rain)
        button_forest = root.findViewById(R.id.nav_music_forest)
        circle_fire = root.findViewById(R.id.circle_fire)
        circle_wind = root.findViewById(R.id.circle_wind)
        circle_water = root.findViewById(R.id.circle_water)
        circle_rain = root.findViewById(R.id.circle_rain)
        circle_forest = root.findViewById(R.id.circle_forest)

        button_forest.setOnClickListener{
            setCircleInvisible()
            circle_forest.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("forest")
            main_text.text = "forest"
        }

        button_wind.setOnClickListener{
            setCircleInvisible()
            circle_wind.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("wind")
            main_text.text = "wind"
        }

        button_water.setOnClickListener{
            setCircleInvisible()
            circle_water.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("water")
            main_text.text = "water"
        }

        button_fire.setOnClickListener{
            setCircleInvisible()
            circle_fire.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("fire")
            main_text.text = "fire"
        }

        button_rain.setOnClickListener{
            setCircleInvisible()
            circle_rain.visibility = View.VISIBLE
            listView.adapter = presenter.getAdapterMusiqueByName("rain")
            main_text.text = "rain"
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
         circle_forest.visibility = View.INVISIBLE
         circle_water.visibility = View.INVISIBLE
         circle_wind.visibility = View.INVISIBLE
         circle_fire.visibility = View.INVISIBLE
         circle_rain.visibility = View.INVISIBLE

         circle!!.visibility = View.INVISIBLE
         if (equalizer != null) {
             equalizer!!.stopBars()
             equalizer!!.visibility = View.INVISIBLE
         }
         presenter.stopMusique()
         musicSelected = -1;
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