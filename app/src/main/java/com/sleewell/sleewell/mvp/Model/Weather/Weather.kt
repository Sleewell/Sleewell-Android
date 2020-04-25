package com.sleewell.sleewell.mvp.Model.Weather

data class Weather(var id: String?) {
    var main: String? = null
    var description: String? = null
    var icon: String? = null
    var rain: Rain? = null
    var clouds: Clouds? = null
    var wind: Wind? = null
    var temp: Float? = null
    var humidity: Int? = null
    var pressure: Int? = null
    var temp_min: Float? = null
    var temp_max: Float? = null
}

class Rain {
    var amount: Int? = null
}

class Wind {
    var speed: Float? = null
    var deg: Float? = null
}

class Clouds {
    var all: Int? = null
}