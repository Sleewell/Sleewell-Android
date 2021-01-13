 package com.sleewell.sleewell.mvp.music.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.sleewell.sleewell.R
import com.sleewell.sleewell.Spotify.SpotifyPlaylist
import com.sleewell.sleewell.mvp.music.MainContract
import com.sleewell.sleewell.mvp.music.presenter.MusicPresenter
import es.claucookie.miniequalizerlibrary.EqualizerView

 /**
 * A simple [Fragment] subclass.
 * Use the [MusicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MusicFragment : Fragment(), MainContract.View  {

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


    private lateinit var spotify_button: Button
    private lateinit var spotify_button_disconneted: Button
    private lateinit var spotify_button_play: Button
    private lateinit var button_reseach_spotify: Button
    private lateinit var playlistSelected: SpotifyPlaylist

    companion object {
        var music_selected: Boolean = false
        lateinit var musicName: String
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
        val spotify_button = root.findViewById<Button>(R.id.MusicButton)

        menu.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_main)
        spotify_button.setOnClickListener {
            navController.navigate(R.id.action_musicFragment_to_spotifyFragment)
        }

        playlistSelected = SpotifyPlaylist("Hollow knight", "spotify:album:4XgGOMRY7H4hl6OQi5wb2Z", "")
        //spotify_button = root.findViewById(R.id.button_spotify)
        //spotify_button_disconneted = root.findViewById(R.id.button_spotify_disconnected)
        //spotify_button_play = root.findViewById(R.id.button_spotify_play)
        //button_reseach_spotify = root.findViewById(R.id.button_reseach_spotify)
        //spotify_button.setOnClickListener{ presenter.connectionSpotify() }
        //spotify_button_disconneted.setOnClickListener{ presenter.disconnectionSpotify() }
        //spotify_button_play.setOnClickListener{
            //    presenter.playPlaylistSpotify(playlistSelected.getUri())
        //}
        //button_reseach_spotify.setOnClickListener{
        //    this.startActivityForResult(Intent(this, SpotifyActivity::class.java), 1000)
        //}
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val name: String = data!!.getStringExtra("nameMusicSelected")
            val uri: String = data!!.getStringExtra("uriMusicSelected")
            playlistSelected = SpotifyPlaylist(name, uri, "")
        }
    }
}