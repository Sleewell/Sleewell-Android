package com.sleewell.sleewell.musique.Model

import android.content.Context
import android.media.MediaPlayer
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import com.sleewell.sleewell.R

import com.sleewell.sleewell.musique.MainContract
import com.sleewell.sleewell.musique.View.MusiqueActivity

/**
 * This class set up the adapter music and can launch a music and stop it
 *
 * @param context context of the app
 * @author gabin warnier de wailly
 */
class MusiqueModel(context: Context) : MainContract.Model {

    private lateinit var list: MutableList<String>
    private lateinit var adapter: ListAdapter
    private var context = context
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        var music_select = 0
    }

    /**
     * This method set the ListAdapter, set all music available in this one
     *
     * @return ListAdapter
     * @author gabin warnier de wailly
     */
    override fun setUpAdapterMusique() : ListAdapter {
        list = ArrayList()

        val fields = R.raw::class.java.fields
        for (i in fields.indices) {
            list.add(fields[i].name)
        }
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
        return adapter
    }

    /**
     * This method launch a music from the ListAdapter set before
     *
     * @param musicInt it's the number of the music from the array list
     * @author gabin warnier de wailly
     */
    override fun startMusique(musicInt: Int) {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
        val singh = context.resources.getIdentifier(list[musicInt], "raw", context.packageName)
        mediaPlayer = MediaPlayer.create(context, singh)
        mediaPlayer!!.start()
        MusiqueActivity.music_select = music_select
    }

    /**
     * This method stop the current music launch
     *
     * @author gabin warnier de wailly
     */
    override fun stopMusique() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
        }
    }
}