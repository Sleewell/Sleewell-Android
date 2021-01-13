package com.sleewell.sleewell.mvp.protocol.presenter

import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.lockScreen.ILockScreenManager
import com.sleewell.sleewell.modules.lockScreen.LockScreenManager
import com.sleewell.sleewell.modules.network.INetworkManager
import com.sleewell.sleewell.modules.network.NetworkManager
import com.sleewell.sleewell.modules.settings.ISettingsManager
import com.sleewell.sleewell.modules.settings.SettingsManager
import com.sleewell.sleewell.mvp.protocol.ProtocolContract
import com.sleewell.sleewell.mvp.protocol.ProtocolMenuContract
import com.sleewell.sleewell.mvp.protocol.model.ProtocolModel

/**
 * Presenter for the protocol activity
 *
 * @constructor Creates an instance of the presenter that link model and view and do all the logic
 * @param view View base on the ProtocolContract.View
 * @param ctx context is from the current activity / view
 * @author Hugo Berthomé
 */
class ProtocolMenuPresenter(private var view: ProtocolMenuContract.View, private val ctx: AppCompatActivity) : ProtocolMenuContract.Presenter {

    private var model: ProtocolContract.Model = ProtocolModel(this, this, ctx)

    private val connection: INetworkManager = NetworkManager(ctx)
    private val lockScreen: ILockScreenManager = LockScreenManager(ctx)
    private val settings: ISettingsManager = SettingsManager(ctx)

    private var nbrBreath: Int = 0
    private val timer = object : CountDownTimer(10000, 10) {

        override fun onTick(millisUntilFinished: Long) {
            if (millisUntilFinished < 6000)
                model.degradesSizeOfCircle()
            else if (millisUntilFinished > 4000)
                model.upgradeSizeOfCircle()
            view.printHalo(model.getSizeOfCircle())
        }
        override fun onFinish() {
            model.resetSizeOfCircle()
            if (nbrBreath > 0) {
                nbrBreath -= 1
                this.start()
            }
        }
    }

    override fun onViewCreated() {
        connection.switchToSleepMode(true)
        lockScreen.enableShowWhenLock()
        lockScreen.enableKeepScreenOn()

        view.printHalo(model.getSizeOfCircle())
        if (settings.getHalo()) {
            startHalo()
        }

        playMusic()

        startAnalyse()
    }

    override fun isHaloOn(): Boolean {
        return settings.getHalo()
    }

    override fun onDestroy() {
        connection.switchToSleepMode(false)
        lockScreen.disableKeepScreenOn()

        model.cleanUp()
    }

    override fun playMusic() {
        if (settings.getMusic()) {
            view.animateEqualizer(true)
        } else {
            view.animateEqualizer(false)
        }
    }

    override fun pauseMusic() {
        if (view.isMusicPlaying()) {
            // TODO: stop the music
            view.animateEqualizer(false) // When you want equalizer stops animating
        } else {
            // TODO: play the music
            view.animateEqualizer(true) // Whenever you want to tart the animation
        }
    }

    override fun startHalo() {
        timer.cancel()
        nbrBreath = 48 // TODO: settings.getHaloTime()
        model.resetSizeOfCircle()
        timer.start()
    }

    override fun stopHalo() {
        timer.cancel()
    }

    override fun disableShowWhenLock() {
        lockScreen.disableShowWhenLock()
    }

    override fun startAnalyse() {
        model.onRecordAudio(true)
    }

    override fun pauseAnalyse() {
        TODO("Not yet implemented")
    }

    override fun resumeAnalyse() {
        TODO("Not yet implemented")
    }

    override fun onAudio(buffer: ShortArray) {
        model.convertToSpectrogram(buffer)
    }

    override fun onAudioError(message: String) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
    }

    override fun onAudioFinished() {
        Toast.makeText(ctx, "Record stopped", Toast.LENGTH_LONG).show()
    }

    override fun onBufferReceived(spectrogram: Array<DoubleArray>) {
        model.analyseAndSave(spectrogram)
    }


    override fun onErrorSpec(msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }
}