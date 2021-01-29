package com.sleewell.sleewell.mvp.protocol.presenter

import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.lockScreen.ILockScreenManager
import com.sleewell.sleewell.modules.lockScreen.LockScreenManager
import com.sleewell.sleewell.mvp.protocol.ProtocolContract
import com.sleewell.sleewell.modules.network.INetworkManager
import com.sleewell.sleewell.modules.network.NetworkManager
import com.sleewell.sleewell.mvp.protocol.model.ProtocolModel

/**
 * Presenter for the protocol activity
 *
 * @constructor Creates an instance of the presenter that link model and view and do all the logic
 * @param view View base on the ProtocolContract.View
 * @param ctx context is from the current activity / view
 * @author Hugo Berthomé
 */
class ProtocolPresenter(view: ProtocolContract.View, private val ctx: AppCompatActivity) : ProtocolContract.Presenter {

    private var view: ProtocolContract.View? = view
    private var model: ProtocolContract.Model = ProtocolModel(this, this, ctx)

    private val connection: INetworkManager = NetworkManager(ctx)
    private val lockScreen: ILockScreenManager = LockScreenManager(ctx)

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

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    override fun onDestroy() {
        connection.switchToSleepMode(false)
        lockScreen.disableShowWhenLock()
        lockScreen.disableKeepScreenOn()
        model.onDestroy()
    }

    /**
     * Function to call at the creation of the view
     *
     * @author Hugo Berthomé
     */
    override fun onViewCreated() {
        connection.switchToSleepMode(true)
        lockScreen.enableShowWhenLock()
        lockScreen.enableKeepScreenOn()
        view?.printHalo(model.getSizeOfCircle())

        startAnalyse()
    }

    override fun startProtocol(number :Int) {
        timer.cancel()
        nbrBreath = number
        model.resetSizeOfCircle()
        timer.start()
    }

    override fun stopProtocol() {
        timer.cancel()
    }

    override fun openDialog() {
        val dialog = model.openColorPicker()
        val yesBtn = dialog.findViewById(R.id.yesBtn) as Button
        val noBtn = dialog.findViewById(R.id.crossImage) as ImageView
        yesBtn.setOnClickListener {
            view?.setColorHalo(model.getColorOfCircle())
            dialog.dismiss()
            view?.hideSystemUI()
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
            view?.hideSystemUI()
        }
        dialog.show()
    }

    /**
     * Start the sleep analyse
     * Will record audio, analyse and save the data from the night in a file
     *
     * @author Hugo Berthomé
     */
    override fun startAnalyse() {
        model.onRecordAudio(true)
    }

    /**
     * Pause the sleep analyse
     *
     * @author Hugo Berthomé
     */
    override fun pauseAnalyse() {
        TODO("Not yet implemented")
    }

    /**
     * Resume the paused sleep analyse
     *
     * @author Hugo Berthomé
     */
    override fun resumeAnalyse() {
        TODO("Not yet implemented")
    }


    /**
     * When a buffer is filled, it will be sent to this callback
     *
     * @param buffer with audio data inside
     * @author Hugo Berthomé
     */
    override fun onAudio(buffer: ShortArray) {
        model.convertToSpectrogram(buffer)
    }

    /**
     * If an error occurred, a message will be sent
     * The record will be stopped
     *
     * @param message - error message
     * @author Hugo Berthomé
     */
    override fun onAudioError(message: String) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
    }

    /**
     * On finished event is called when the recording is stopped
     * (not called when an error occurred but onError instead)
     *
     * @author Hugo Berthomé
     */
    override fun onAudioFinished() {
        Toast.makeText(ctx, "Record stopped", Toast.LENGTH_LONG).show()
    }

    /**
     * Function called in async when a list of spectrogram windows has been calculated
     *
     * @param spectrogram
     */
    override fun onBufferReceived(spectrogram: Array<DoubleArray>) {
        model.analyseAndSave(spectrogram)
    }

    /**
     * Function called when an error occurred
     *
     * @param msg
     */
    override fun onErrorSpec(msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }
}