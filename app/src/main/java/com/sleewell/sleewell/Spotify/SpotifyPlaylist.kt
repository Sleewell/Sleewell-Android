package com.sleewell.sleewell.Spotify

class SpotifyPlaylist(name: String, uri: String, image: String) {
    private var name: String = name
    private var uri: String = uri
    private var urlImage: String = image

    fun getName() : String { return name }
    fun getUri() : String { return uri }
    fun getUrlImage() : String { return urlImage }
}