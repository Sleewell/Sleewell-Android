package com.sleewell.sleewell.mvp.menu.routine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sleewell.sleewell.R.drawable.*
import com.sleewell.sleewell.database.routine.entities.Routine
import com.sleewell.sleewell.mvp.spotify.LoaderUrlImage


class RoutineListAdapter(private var context: Context, private var aList: ArrayList<Routine>) : BaseAdapter()  {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = inflater.inflate(com.sleewell.sleewell.R.layout.listview_routine_item, parent, false)
        val holder = HolderRoutine(view)


        if (aList[position].name.isEmpty()) {
            holder.title!!.text = "Routine " + aList[position].apiId.toString()
        } else {
            holder.title!!.text = aList[position].name
        }

        if (aList[position].imagePlaylist.isNotEmpty()) {
            LoaderUrlImage().downloadImage(context, aList[position].imagePlaylist, holder.image)
        }

        if (aList[position].useMusic) {
            holder.music!!.setBackgroundResource(ic_music_on)
            holder.musicTitle!!.visibility = View.VISIBLE
            if (aList[position].player == "Sleewell")
                holder.musicTitle!!.text = aList[position].musicName.split("_").last()
            else
                holder.musicTitle!!.text = aList[position].musicName
        } else {
            holder.music!!.setBackgroundResource(ic_music_off)
            holder.musicTitle!!.visibility = View.INVISIBLE
        }

        if (aList[position].useHalo) {
            val r = aList[position].colorRed
            val g = aList[position].colorGreen
            val b = aList[position].colorBlue

            holder.haloIcon!!.setBackgroundResource(ic_halo)

            holder.halo!!.visibility = View.VISIBLE

            holder.halo!!.setBackgroundColor(Color.rgb(r, g, b))
        } else {
            holder.haloIcon!!.setBackgroundResource(ic_halo_off)
            holder.halo!!.visibility = View.INVISIBLE
        }

        if (aList[position].isSelected) {
            holder.selected!!.visibility = View.VISIBLE
        } else {
            holder.selected!!.visibility = View.GONE
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return aList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return aList.size
    }
}