package com.sleewell.sleewell.mvp.spotify

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso


class LoaderUrlImage {

    fun downloadImage(c: Context?, url: String?, img: ImageView?) {
        if (url != null && url.isNotEmpty()) {
            Picasso.get().load(url).placeholder(com.sleewell.sleewell.R.drawable.playlistholder).into(img)
        } else {
            Picasso.get().load(com.sleewell.sleewell.R.drawable.playlistholder).into(img)
        }
    }
}