package com.sleewell.sleewell.mvp.music

class MusicPlaylist(title: String, description: String, image: String, name: String) {
    private var title: String = title
    private var description: String = description
    private var name: String = name

    fun getTitle() : String { return title }
    fun getDescription() : String { return description }
    fun getName() : String { return name }
}