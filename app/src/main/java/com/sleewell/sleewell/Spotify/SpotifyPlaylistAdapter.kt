package com.sleewell.sleewell.Spotify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


class SpotifyPlaylistAdapter(context: Context, aList: ArrayList<SpotifyPlaylist>) : BaseAdapter() {

    private var context: Context = context
    private var aList: ArrayList<SpotifyPlaylist> = aList
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = inflater.inflate(com.sleewell.sleewell.R.layout.listview_spotify_item, parent, false)

        val holder = MyHolder(view)
        holder.name!!.text = aList[position].getName()
        //holder.uri!!.text = aList[position].getUri()
        LoaderUrlImage().downloadImage(context, aList[position].getUrlImage(), holder.img)
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