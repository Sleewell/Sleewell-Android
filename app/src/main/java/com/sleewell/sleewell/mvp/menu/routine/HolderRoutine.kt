package com.sleewell.sleewell.mvp.menu.routine

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sleewell.sleewell.R

class HolderRoutine(v: View?)  {

    var title: TextView? = v!!.findViewById(R.id.item_routine_title)
    var image: ImageView? = v!!.findViewById(R.id.item_routine_images)
    var haloIcon: ImageView? = v!!.findViewById(R.id.item_routine_halo_icon)
    var halo: ImageView? = v!!.findViewById(R.id.item_routine_halo)
    var music: ImageView? = v!!.findViewById(R.id.item_routine_music)
    var musicTitle: TextView? = v!!.findViewById(R.id.item_routine_music_title)
    var selected: ImageView? = v!!.findViewById(R.id.item_routine_is_selected)
}