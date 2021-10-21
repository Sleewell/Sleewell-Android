package com.sleewell.sleewell.mvp.protocol.presenter

import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.lockScreen.ILockScreenManager
import com.sleewell.sleewell.modules.lockScreen.LockScreenManager
import com.sleewell.sleewell.modules.network.INetworkManager
import com.sleewell.sleewell.modules.network.NetworkManager
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

class ProtocolPresenter(private var view: ProtocolMenuContract.View, private val ctx: AppCompatActivity) : ProtocolMenuContract.Presenter {

    private var model: ProtocolMenuContract.Model = ProtocolModel(ctx)

    private val connection: INetworkManager = NetworkManager(ctx)
    private val lockScreen: ILockScreenManager = LockScreenManager(ctx)

    private var nbrBreath: Int = 0
    private val timer = object : CountDownTimer(10000, 10) {

        override fun onTick(millisUntilFinished: Long) {
            if (millisUntilFinished < 6000)
                model.degradesSizeOfCircle()
            else if (millisUntilFinished > 4000)
                model.upgradeSizeOfCircle()
            view.setHaloColor(model.getroutineColorHalo())
            view.printHalo(model.getSizeOfCircle())
        }
        override fun onFinish() {
            model.resetSizeOfCircle()
            if (nbrBreath > 0) {
                nbrBreath -= 1
                this.start()
            } else if (nbrBreath <= 0)
                finishProtocol()
        }
    }

    fun finishProtocol() {
        model.stopMusic()
        lockScreen.disableKeepScreenOn()
    }

    override fun onViewCreated() {
        connection.switchToSleepMode(true)
        lockScreen.enableShowWhenLock()
        lockScreen.enableKeepScreenOn()
        view.hideSystemUI()

        model.setRoutineSelected(::startRoutine)

        startAnalyse()
    }

    private fun startRoutine() {
        if (model.routineUseHalo()) {
            startHalo()
        }
        if (model.routineUseMusic()) {
            playMusic()
        }
        view.haloDisplayLooper()
    }

    override fun isHaloOn(): Boolean {
        return model.routineUseHalo()
    }

    override fun onDestroy() {
        connection.switchToSleepMode(false)
        lockScreen.disableKeepScreenOn()
        lockScreen.disableShowWhenLock()
        view.showSystemUI()

        stopAnalyse()

        model.stopMusic()
        model.onDestroy()
    }

    override fun playMusic() {
        if (model.routineUseMusic()) {
            view.animateEqualizer(true)
            if (model.getRoutinePlayer() == "Spotify")
                model.loginSpotify()
            else
                model.playMusic()
        } else {
            view.animateEqualizer(false)
            model.stopMusic()
        }
    }

    override fun pauseMusic() {
        if (!model.routineUseMusic())
            return
        if (view.isMusicPlaying()) {
            model.pauseMusic()
            view.animateEqualizer(false) // When you want equalizer stops animating
        } else {
            model.resumeMusic()
            view.animateEqualizer(true) // Whenever you want to tart the animation
        }
    }

    override fun startHalo() {
        timer.cancel()
        nbrBreath = 48 // TODO: settings.getHaloTime()
        model.resetSizeOfCircle()
        view.setHaloColor(model.getroutineColorHalo())
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

    override fun stopAnalyse() {
        model.onRecordAudio(false)
    }
}