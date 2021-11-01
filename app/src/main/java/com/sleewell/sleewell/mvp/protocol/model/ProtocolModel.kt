package com.sleewell.sleewell.mvp.protocol.model

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.database.routine.RoutineDao
import com.sleewell.sleewell.database.routine.RoutineDatabase
import com.sleewell.sleewell.modules.audio.service.AnalyseService
import com.sleewell.sleewell.modules.audio.service.AnalyseServiceTracker
import com.sleewell.sleewell.mvp.protocol.ProtocolMenuContract
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * this class is the model of the halo
 *
 * @param context current context of the app
 * @author gabin warnier de wailly
 */
class ProtocolModel(
    private val context: AppCompatActivity
) : ProtocolMenuContract.Model {
    private var size: Int = 10
    var db: RoutineDao = RoutineDatabase.getDatabase(context).routineDao()

    private var routineColorRed: Int = 0
    private var routineColorGreen: Int = 0
    private var routineColorBlue: Int = 0
    private var routineUseHalo: Boolean = false
    private var routineDuration: Int = 0
    private var routineUseMusic: Boolean = false
    private var routinePlayer: String = "None"
    private var routineMusicName: String = ""
    private var routineMusicUri: String = ""

    //Music
    private var mediaPlayer: MediaPlayer? = null

    //Spotify
    private val clientId = "" // /!\ need to hide
    private val redirectUri = "http://com.sleewell.sleewell/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun getSizeOfCircle(): Int {
        return size
    }

    override fun upgradeSizeOfCircle() {
        if (size < 1000)
            size += 3
    }

    override fun degradesSizeOfCircle() {
        if (size > 10 && size - 2 > 10)
            size -= 2
    }

    override fun resetSizeOfCircle() {
        size = 10
    }

    /**
     * Record the audio from the mic source
     *
     * @param state
     * @author Hugo Berthomé
     */
    override fun onRecordAudio(state: Boolean) {
        if (state) {
            startForeground()
        } else {
            stopForeground()
        }
    }

    /**
     * Return if the smartphone is recording
     *
     * @return True if recording, False otherwise
     * @author Hugo Berthomé
     */
    override fun isRecording(): Boolean {
        return AnalyseServiceTracker.getServiceState(context) == AnalyseServiceTracker.ServiceState.STARTED
    }

    override fun playMusic() {
        if (routinePlayer == "Sleewell") {
            if (mediaPlayer != null) {
                mediaPlayer!!.release()
            }
            val song = context.resources.getIdentifier(routineMusicName, "raw", context.packageName)
            mediaPlayer = MediaPlayer.create(context, song)
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.start()
        } else if (routinePlayer == "Spotify") {
            if (routineMusicName.isNotEmpty() && routineMusicUri.isNotEmpty()) {
                spotifyAppRemote?.playerApi?.play(routineMusicUri)
            }
        }
    }

    override fun pauseMusic() {
        if (routinePlayer == "Sleewell") {
            if (mediaPlayer != null)
                mediaPlayer!!.pause()
        } else if (routinePlayer == "Spotify"){
            if (spotifyAppRemote != null)
                spotifyAppRemote?.playerApi?.pause()
        }
    }

    override fun resumeMusic() {
        if (routinePlayer == "Sleewell") {
            if (mediaPlayer != null)
                mediaPlayer!!.start()
        } else if (routinePlayer == "Spotify"){
            if (spotifyAppRemote != null)
                spotifyAppRemote?.playerApi?.resume()
        }
    }

    override fun stopMusic() {
        if (routinePlayer == "Sleewell") {
            if (mediaPlayer != null)
                mediaPlayer!!.stop()
        } else if (routinePlayer == "Spotify"){
            if (spotifyAppRemote != null)
                spotifyAppRemote?.playerApi?.pause()
        }
    }

    /**
     * Method to call at the end of the view
     *
     */
    override fun onDestroy() {
        stopMusic()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }


    /**
     * Start the foreground service and the analyse
     *
     * @author Hugo Berthomé
     */
    private fun startForeground() {
        Intent(context, AnalyseService::class.java).also {
            it.action = AnalyseService.START
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                this.context.startForegroundService(it)
            } else {
                this.context.startService(it)
            }
        }
    }

    /**
     * Stop the analyse and the foreground service
     *
     * @author Hugo Berthomé
     */
    private fun stopForeground() {
        if (AnalyseServiceTracker.getServiceState(context) != AnalyseServiceTracker.ServiceState.STARTED)
            return
        with(Intent(context, AnalyseService::class.java)) {
            action = AnalyseService.STOP
            context.startService(this)
        }
    }

    override fun setRoutineSelected(startRoutine: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var routines = db.getRoutineSelected()
            if (routines.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    routineColorRed = routines[0].colorRed
                    routineColorGreen = routines[0].colorGreen
                    routineColorBlue = routines[0].colorBlue
                    routineUseHalo = routines[0].useHalo
                    routineDuration = routines[0].duration
                    routineUseMusic = routines[0].useMusic
                    routinePlayer = routines[0].player
                    routineMusicName = routines[0].musicName
                    routineMusicUri = routines[0].musicUri
                    CoroutineScope(Dispatchers.Main).launch {
                        startRoutine()
                    }
                }
            }
        }
    }

    override fun routineUseHalo() : Boolean {
        return routineUseHalo
    }

    override fun routineUseMusic() : Boolean {
        return routineUseMusic
    }

    override fun getroutineColorHalo() : Int {
        return Color.rgb(routineColorRed, routineColorGreen, routineColorBlue)
    }

    override fun getRoutinePlayer() : String {
        return routinePlayer
    }

    override fun loginSpotify() {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {

            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show()
                playMusic()
            }

            override fun onFailure(throwable: Throwable) {
                Toast.makeText(context, "Fail " + throwable.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}