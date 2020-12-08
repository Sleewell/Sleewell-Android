package com.sleewell.sleewell.mvp.home.presenter

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.audio.audioPlayer.IPlayerManager
import com.sleewell.sleewell.modules.audio.audioPlayer.PlayerManager
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderManager
import com.sleewell.sleewell.modules.audio.audioRecord.RecorderManager
import com.sleewell.sleewell.mvp.home.HomeContract
import com.sleewell.sleewell.mvp.home.model.HomeModel
import com.sleewell.sleewell.mvp.home.model.NfcState

/**
 * Presenter for the Home fragment, it will link the HomeView and the HomeModel
 *
 * @constructor Creates a presenter based on the Home Contract
 * @param view View that inherits the View from the home contract
 * @param context Context of the activity / view
 * @author Hugo Berthomé
 */
class HomePresenter(view: HomeContract.View, private var context: AppCompatActivity) : HomeContract.Presenter {
    private var view: HomeContract.View? = view
    private var model: HomeContract.Model = HomeModel(context)
    private var recorder: IRecorderManager = RecorderManager(context)
    private var player: IPlayerManager = PlayerManager(context)
    private var isRecording: Boolean = false;

    private var filePath = "${context.cacheDir?.absolutePath}"

    /**
     * Function to call at the creation of the view
     *
     * @author Hugo Berthomé
     */
    override fun onViewCreated() {
        view?.displayNfcButton(model.nfcState() != NfcState.Enable)
        recorder.setOutputFile(filePath, "audioRecordTest", ".3gp")
    }

    /**
     * Function to call on resume of the view
     *
     * @author Hugo Berthomé
     */
    override fun onViewResume() {

    }

    /**
     * Start of stop record of the sound from the device
     *
     * @author Hugo Berthomé
     */
    override fun onRecordClick() {
        if (!recorder.permissionGranted())
            recorder.askPermission()
        if (recorder.permissionGranted()) {
            isRecording = !isRecording
            recorder.onRecord(isRecording)
            view?.displayRecordState(isRecording)
        }
    }

    /**
     * Start / Pause the audio player
     *
     * @author Hugo Berthomé
     */
    override fun onPlayClick() {
        if (player.isInitialized()) {
            view?.displayPlayerState(false)
            player.destroy()
        } else {
            player.initializeFile(filePath)
            player.start()
            view?.displayPlayerState(true)
        }
    }

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    override fun onDestroy() {
        view = null
    }
}