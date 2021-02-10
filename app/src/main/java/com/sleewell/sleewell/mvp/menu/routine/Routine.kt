package com.sleewell.sleewell.mvp.menu.routine

class Routine(red: Int, green: Int, blue: Int, halo: Boolean, duration: Int, music: Boolean, play: musicPlayer) {

    // Color
    private var colorRed: Int = red
    private var colorGreen: Int = green
    private var colorBlue: Int = blue

    // Duration
    private var useHalo: Boolean = halo
    private var duration: Int = duration

    // Music
    private var useMusic: Boolean = music
    private var player: musicPlayer = play

    // Color
    fun getColorRed() : Int { return colorRed }
    fun getColorGreen() : Int { return colorGreen }
    fun getColorBlue() : Int { return colorBlue }

    fun setColorRed(color: Int) { colorRed = color }
    fun setColorGreen(color: Int) { colorGreen = color }
    fun setColorBlue(color: Int) { colorBlue = color }


    // Duration
    fun IsUseHalo() : Boolean { return useHalo}
    fun getDuration() : Int { return duration }

    fun setUseHalo(use: Boolean) { useHalo = use }
    fun setDuration(time: Int) { duration = time }


    // Music
    fun IsUseMusic() : Boolean { return useMusic }
    fun getMusicPlayer() : musicPlayer { return player }

    fun setUseMusic( use : Boolean) { useMusic = use }
    fun setMusicPlayer(play: musicPlayer) { player = play }


    enum class musicPlayer {
        MUSIC, SPOTIFY
    }
}
