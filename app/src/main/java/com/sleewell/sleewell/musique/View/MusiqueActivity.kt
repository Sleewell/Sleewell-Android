package com.sleewell.sleewell.musique.View

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
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
}
