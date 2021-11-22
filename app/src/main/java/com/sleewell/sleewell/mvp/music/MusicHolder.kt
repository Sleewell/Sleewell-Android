package com.sleewell.sleewell.mvp.music

import android.view.View
import android.widget.TextView
import com.sleewell.sleewell.R

class MusicHolder(v: View?) {

    var title: TextView? = v!!.findViewById(R.id.soundTitle)
    var description: TextView? = v!!.findViewById(R.id.soundDescription)
    //var image: ImageView? = v!!.findViewById(R.id.listview_spotify_images)
}