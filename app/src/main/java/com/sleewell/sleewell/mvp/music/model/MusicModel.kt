package com.sleewell.sleewell.mvp.music.model

import android.content.Context
import android.media.MediaPlayer
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.music.MainContract
import com.sleewell.sleewell.mvp.music.MusicPlaylist
import com.sleewell.sleewell.mvp.music.MusicPlaylistAdapter

/**
 * This class set up the adapter music and can launch a music and stop it
 *
 * @param context context of the app
 * @author gabin warnier de wailly
 */
class MusicModel(context: Context) : MainContract.Model {

    private var aList: ArrayList<MusicPlaylist> = ArrayList()
    private lateinit var adapter: MusicPlaylistAdapter
    private var context = context
    private var mediaPlayer: MediaPlayer? = null

    /**
     * This method set the ListAdapter, set all music available in this one
     *
     * @return ListAdapter
     * @author gabin warnier de wailly
     */
    override fun setUpAdapterMusiqueByName(name: String) : MusicPlaylistAdapter {
        aList = ArrayList()

        val fields = R.raw::class.java.fields
        for (i in fields.indices) {
            val musicInfo = fields[i].name.split("_").toTypedArray()
            if (musicInfo[0] == name || name == "") {
                val title = musicInfo[1]
                val description = "$title sound"
                val image = ""
                val name = fields[i].name
                aList.add(MusicPlaylist(title, description, image, name))
            }
        }
        adapter = MusicPlaylistAdapter(context, aList)
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
        val singh = context.resources.getIdentifier(aList[musicInt].getName(), "raw", context.packageName)
        mediaPlayer = MediaPlayer.create(context, singh)
        mediaPlayer!!.start()
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