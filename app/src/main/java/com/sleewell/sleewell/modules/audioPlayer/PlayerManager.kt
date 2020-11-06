package com.sleewell.sleewell.modules.audioPlayer

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity

class PlayerManager(private val ctx: AppCompatActivity) : IPlayerManager {
    private var player : MediaPlayer? = null

    /**
     * Initialize the media player with the corresponding file
     *
     * @param filePath
     * @author Hugo Berthomé
     */
    override fun initializeFile(filePath: String) {
        player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(filePath)
            prepare()
        }
    }

    /**
     * Return if the player has been initialized
     *
     * @return true if initialized otherwise false
     * @author Hugo Berthomé
     */
    override fun isInitialized(): Boolean {
        return player != null
    }

    /**
     * Returns if the media is playing
     *
     * @author Hugo Berthomé
     */
    override fun isPlaying() : Boolean? {
        return player?.isPlaying
    }

    /**
     * Start or resume the media
     *
     * @author Hugo Berthomé
     */
    override fun start() {
        player?.start()
    }

    /**
     * Pause the media
     *
     * @author Hugo Berthomé
     */
    override fun pause() {
        player?.pause()
    }

    /**
     * Stop the media and put it at the beginning
     *
     * @author Hugo Berthomé
     */
    override fun stop() {
        player?.stop()
    }

    /**
     * Destroy the media player
     *
     * @author Hugo Berthomé
     */
    override fun destroy() {
        player?.stop()
        player?.release()
        player = null
    }
}