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


class RoutineListAdapter(context: Context, aList: ArrayList<Routine>) : BaseAdapter()  {

    private var context: Context = context
    private var aList: ArrayList<Routine> = aList
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

        if (aList[position].useMusic) {
            holder.music!!.setBackgroundResource(ic_music_on)
            holder.music_title!!.visibility = View.VISIBLE
            if (aList[position].player == "Sleewell")
                holder.music_title!!.text = aList[position].musicName.split("_").last()
            else
                holder.music_title!!.text = aList[position].musicName
        } else {
            holder.music!!.setBackgroundResource(ic_music_off)
            holder.music_title!!.visibility = View.INVISIBLE
        }

        if (aList[position].useHalo) {
            val r = aList[position].colorRed
            val g = aList[position].colorGreen
            val b = aList[position].colorBlue

            holder.halo_icon!!.setBackgroundResource(ic_halo)

            holder.halo!!.visibility = View.VISIBLE

            holder.halo!!.setBackgroundColor(Color.rgb(r, g, b))
        } else {
            holder.halo_icon!!.setBackgroundResource(ic_halo_off)
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