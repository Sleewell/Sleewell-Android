package com.sleewell.sleewell.api.openWeather

import com.google.gson.annotations.SerializedName

data class ApiResult(var id: String?) {
    @SerializedName("coord")
    var coord: Cord? = null

    @SerializedName("weather")
    var weather: Array<Weather>? = null

    @SerializedName("base")
    var base: String? = null

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("visibility")
    var visibility: String? = null

    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("clouds")
    var clouds: Clouds? = null

    @SerializedName("dt")
    var dt: Long? = null

    @SerializedName("name")
    var name: String? = null
}

class Cord {
    @SerializedName("lon")
    var lon: Float? = null

    @SerializedName("lat")
    var lat: Float? = null
}

class Weather {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("main")
    var main: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("icon")
    var icon: String? = null
}

class Main {
    @SerializedName("temp")
    var temp: Float? = null

    @SerializedName("pressure")
    var pressure: Int? = null

    @SerializedName("humidity")
    var humidity: Int? = null

    @SerializedName("temp_min")
    var tempMin: Float? = null

    @SerializedName("temp_max")
    var tempMax: Float? = null
}

class Wind {
    @SerializedName("speed")
    var speed: Float? = null

    @SerializedName("deg")
    var deg: Float? = null
}

class Clouds {
    @SerializedName("all")
    var all: Int? = null
}