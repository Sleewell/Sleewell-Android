package com.sleewell.sleewell.musique.Model

import android.content.Context
import android.media.MediaPlayer
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import com.sleewell.sleewell.R

import com.sleewell.sleewell.musique.MainContract
import com.sleewell.sleewell.musique.View.MusiqueActivity

class MusiqueModel(context: Context) : MainContract.Model {

    private lateinit var list: MutableList<String>
    private lateinit var adapter: ListAdapter
    private var context = context
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        var music_select = 0
    }

    override fun setUpAdapterMusique() : ListAdapter {
        list = ArrayList()

        val fields = R.raw::class.java.fields
        for (i in fields.indices) {
            list.add(fields[i].name)
        }
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
        return adapter
    }

    override fun startMusique(musicInt: Int) {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
        val singh = context.resources.getIdentifier(list[musicInt], "raw", context.packageName)
        mediaPlayer = MediaPlayer.create(context, singh)
        mediaPlayer!!.start()
        MusiqueActivity.music_select = music_select
    }

    override fun stopMusique() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
        }
    }
}