package com.sleewell.sleewell.mvp.protocol.presenter

import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.lockScreen.ILockScreenManager
import com.sleewell.sleewell.modules.lockScreen.LockScreenManager
import com.sleewell.sleewell.modules.network.INetworkManager
import com.sleewell.sleewell.modules.network.NetworkManager
import com.sleewell.sleewell.modules.settings.ISettingsManager
import com.sleewell.sleewell.modules.settings.SettingsManager
import com.sleewell.sleewell.mvp.music.view.MusicFragment
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

    private var model: ProtocolMenuContract.Model = ProtocolModel(ctx)

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
        view.hideSystemUI()

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
        view.showSystemUI()

        model.stopMusique()
        model.onDestroy()
    }

    override fun playMusic() {
        if (settings.getMusic()) {
            view.animateEqualizer(true)
            if (MusicFragment.music_selected) {
                val name = MusicFragment.musicName
                if (name != null) {
                    model.startMusique(name)
                }
            }
        } else {
            view.animateEqualizer(false)
            model.stopMusique()
        }
    }

    override fun pauseMusic() {
        if (view.isMusicPlaying()) {
            model.pauseMusique()
            view.animateEqualizer(false) // When you want equalizer stops animating
        } else {
            model.resumeMusique()
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

    /**
     * Stop the analyse
     *
     */
    override fun stopAnalyse() {
        model.onRecordAudio(false)
    }
}