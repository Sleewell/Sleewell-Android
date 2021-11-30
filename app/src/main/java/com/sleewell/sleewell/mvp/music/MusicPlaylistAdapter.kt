package com.sleewell.sleewell.mvp.music

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class MusicPlaylistAdapter(context: Context, private var aList: ArrayList<MusicPlaylist>) : BaseAdapter()  {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = inflater.inflate(com.sleewell.sleewell.R.layout.listview_music_item, parent, false)

        val holder = MusicHolder(view)
        holder.title!!.text = aList[position].getTitle()
        holder.description!!.text = aList[position].getDescription()
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