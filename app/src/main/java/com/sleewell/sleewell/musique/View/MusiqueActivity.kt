package com.sleewell.sleewell.musique.View

import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.musique.MainContract
import com.sleewell.sleewell.musique.Presenter.MusiquePresenter


/**
 * This class interact with the user, display everything and catch every action of the user
 *
 * @author gabin warnier de wailly
 */
class MusiqueActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private  lateinit var listView: ListView

    private lateinit var spotify_button: Button
    private lateinit var spotify_button_disconneted: Button
    private lateinit var spotify_button_play: Button


    companion object {
        var music_select = 0
    }


    /**
     * This method setup the view
     *
     * @param savedInstanceState creation of the view
     * @author gabin warnier de wailly
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musique)

        setPresenter(MusiquePresenter(this, this))
        InitActivityWidgets()
        presenter.onViewCreated()
    }

    /**
     * This method  setup every widgets created
     *
     * @author gabin warnier de wailly
     */
    private fun InitActivityWidgets() {

        listView = findViewById(R.id.music_list_view)
        listView.adapter = presenter.getAdapterMusique()
        listView.onItemClickListener = AdapterView.OnItemClickListener{ _, _, i, _ ->
            presenter.launchMusique(i)
        }

        spotify_button = findViewById(R.id.button_spotify)
        spotify_button_disconneted = findViewById(R.id.button_spotify_disconnected)
        spotify_button_play = findViewById(R.id.button_spotify_play)
        spotify_button.setOnClickListener{ presenter.connectionSpotify() }
        spotify_button_disconneted.setOnClickListener{ presenter.disconnectionSpotify() }
        spotify_button_play.setOnClickListener{ presenter.playPlaylistSpotify("spotify:playlist:3vb8KWLwZhSRmr5vjCoqYk") }
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
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}