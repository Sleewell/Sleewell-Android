package com.sleewell.sleewell.mvp.music

class MusicPlaylist(private var title: String, private var description: String, image: String, private var name: String) {

    fun getTitle() : String { return title }
    fun getDescription() : String { return description }
    fun getName() : String { return name }
}