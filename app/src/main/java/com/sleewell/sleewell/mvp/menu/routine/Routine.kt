package com.sleewell.sleewell.mvp.menu.routine

class Routine {

    // Color
    private var colorRed: Int = 0
    private var colorGreen: Int = 0
    private var colorBlue: Int = 0

    // Duration
    private var useHalo: Boolean = false
    private var duration: Int = 0

    // Music
    private var useMusic: Boolean = false
    private var player: musicPlayer = musicPlayer.MUSIC

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
