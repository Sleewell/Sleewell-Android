package com.sleewell.sleewell.mvp.spotify

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sleewell.sleewell.R

class MyHolder(v: View?) {

    var name: TextView? = v!!.findViewById(R.id.listview_spotify_title)
    //var uri: TextView? = v!!.findViewById(R.id.listview_spotify_uri)
    var img: ImageView? = v!!.findViewById(R.id.listview_spotify_images)

}