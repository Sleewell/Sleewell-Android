package com.sleewell.sleewell.Spotify

import com.google.gson.annotations.SerializedName
import java.util.*

data class ApiResultSpotify(var id: String?) {
    @SerializedName("playlists")
    var playlists: Playlists? = null
}

class Playlists {
    @SerializedName("href")
    var href: String? = null

    @SerializedName("items")
    var items: Array<Item>? = null

    @SerializedName("limit")
    var limit: Int? = null

    @SerializedName("next")
    var next: String? = null

    @SerializedName("offset")
    var offset: Int? = null

    @SerializedName("previous")
    var previous: Objects? = null

    @SerializedName("total")
    var total: Int? = null
}

class Item {
    @SerializedName("collaborative")
    var collaborative: Boolean? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("external_urls")
    var external_urls: ExternalUrls? = null

    @SerializedName("href")
    var href: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("images")
    var images: Array<Image>? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("owner")
    var owner: Owner? = null

    @SerializedName("primary_color")
    var primary_color: Objects? = null

    @SerializedName("public")
    var public: Objects? = null

    @SerializedName("snapshot_id")
    var snapshot_id: String? = null

    @SerializedName("tracks")
    var tracks: Tracks? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("uri")
    var uri: String? = null
}

class Tracks {
    @SerializedName("href")
    var href: String? = null

    @SerializedName("total")
    var total: Int? = null
}

class Owner {
    @SerializedName("display_name")
    var display_name: String? = null

    @SerializedName("external_urls")
    var external_urls: ExternalUrls? = null

    @SerializedName("href")
    var href: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("uri")
    var uri: String? = null
}

class ExternalUrls {
    @SerializedName("spotify")
    var spotify: String? = null
}

class Image {
    @SerializedName("height")
    var height: Int? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("width")
    var width: Int? = null
}