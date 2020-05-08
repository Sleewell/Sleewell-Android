package com.sleewell.sleewell.mvc.View

interface View {
    fun displayWeatherState(imageUrl: String)

    fun displayWaitingState()

    fun showToast(msg: String)
}