package com.sleewell.sleewell.modules.audioPlayer

interface IPlayerManager {

    /**
     * Initialize the media player with the corresponding file
     *
     * @param filePath
     * @author Hugo Berthomé
     */
    fun initializeFile(filePath: String)

    /**
     * Return if the player has been initialized
     *
     * @return true if initialized otherwise false
     * @author Hugo Berthomé
     */
    fun isInitialized() : Boolean

    /**
     * Returns if the media is playing
     *
     * @author Hugo Berthomé
     */
    fun isPlaying() : Boolean?

    /**
     * Start or resume the media
     *
     * @author Hugo Berthomé
     */
    fun start()

    /**
     * Pause the media
     *
     * @author Hugo Berthomé
     */
    fun pause()

    /**
     * Stop the media and put it at the beginning
     *
     * @author Hugo Berthomé
     */
    fun stop()

    /**
     * Destroy the media player
     *
     * @author Hugo Berthomé
     */
    fun destroy()
}