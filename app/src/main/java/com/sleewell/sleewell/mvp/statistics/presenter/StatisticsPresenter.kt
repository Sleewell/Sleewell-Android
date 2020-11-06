package com.sleewell.sleewell.mvp.statistics.presenter

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.mvp.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.statistics.model.StatisticsModel
import java.nio.Buffer
import java.nio.ByteBuffer

const val LOG_TAG = "StatisticsPresenter"

class StatisticsPresenter(private var view: StatisticsContract.View, private var context: AppCompatActivity) : StatisticsContract.Presenter {
    private val model: StatisticsContract.Model = StatisticsModel(this, context)

    override fun onStartClick() {
        model.onRecord(!model.isRecording())
    }

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    /**
     * When a buffer is filled, it will be sent to this callback
     *
     * @param buffer with audio data inside
     * @author Hugo Berthomé
     */
    override fun onAudio(buffer: ByteBuffer) {
        var array = buffer.array()
        Log.v("OK", "OK")
    }

    /**
     * If an error occurred, a message will be sent
     * The record will be stopped
     *
     * @param message - error message
     * @author Hugo Berthomé
     */
    override fun onError(message: String) {
        view.displayToast(message)
    }

    /**
     * On finished event is called when the recording is stopped
     * (not called when an error occurred but onError instead)
     *
     * @author Hugo Berthomé
     */
    override fun onFinished() {
        view.displayToast("Thread finished after 2000 milliseconds")
    }
}