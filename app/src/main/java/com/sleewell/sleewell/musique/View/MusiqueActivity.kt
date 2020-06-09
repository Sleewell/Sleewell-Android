package com.sleewell.sleewell.musique.View

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.musique.MainContract
import com.sleewell.sleewell.musique.Presenter.MusiquePresenter

class MusiqueActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private  lateinit var listView: ListView

    companion object {
        var music_select = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musique)

        setPresenter(MusiquePresenter(this, this))
        InitActivityWidgets()
        presenter.onViewCreated()
    }

    private fun InitActivityWidgets() {

        listView = findViewById(R.id.music_list_view)
        listView.adapter = presenter.getAdapterMusique()
        listView.onItemClickListener = AdapterView.OnItemClickListener{ _, _, i, _ ->
            presenter.launchMusique(i)
        }
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
