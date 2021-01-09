package com.sleewell.sleewell.Spotify

import android.R
import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso


class LoaderUrlImage {

    fun downloadImage(c: Context?, url: String?, img: ImageView?) {
        if (url != null && url.length > 0) {
            Picasso.with(c).load(url).placeholder(com.sleewell.sleewell.R.drawable.playlistholder).into(img)
        } else {
            Picasso.with(c).load(com.sleewell.sleewell.R.drawable.playlistholder).into(img)
        }
    }
}