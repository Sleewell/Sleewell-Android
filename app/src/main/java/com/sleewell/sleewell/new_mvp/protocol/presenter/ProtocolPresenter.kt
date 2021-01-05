package com.sleewell.sleewell.new_mvp.protocol.presenter

import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.lockScreen.ILockScreenManager
import com.sleewell.sleewell.modules.lockScreen.LockScreenManager
import com.sleewell.sleewell.modules.network.INetworkManager
import com.sleewell.sleewell.modules.network.NetworkManager
import com.sleewell.sleewell.new_mvp.protocol.ProtocolContract
import com.sleewell.sleewell.new_mvp.protocol.model.ProtocolModel

/**
 * Presenter for the protocol activity
 *
 * @constructor Creates an instance of the presenter that link model and view and do all the logic
 * @param view View base on the ProtocolContract.View
 * @param ctx context is from the current activity / view
 * @author Hugo Berthomé
 */
class ProtocolPresenter(view: ProtocolContract.View, ctx: AppCompatActivity) : ProtocolContract.Presenter {

    private var view: ProtocolContract.View? = view
    private val connection: INetworkManager = NetworkManager(ctx)
    private val lockScreen: ILockScreenManager = LockScreenManager(ctx)
    private var model: ProtocolContract.Model = ProtocolModel(ctx)
    private var nbrBreath: Int = 0
    private val timer = object : CountDownTimer(10000, 10) {

        override fun onTick(millisUntilFinished: Long) {
            if (millisUntilFinished < 6000)
                model.degradesSizeOfCircle()
            else if (millisUntilFinished > 4000)
                model.upgradeSizeOfCircle()
            view?.printHalo(model.getSizeOfCircle())
        }
        override fun onFinish() {
            model.resetSizeOfCircle()
            if (nbrBreath > 0) {
                nbrBreath = nbrBreath  - 1
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
    }

    /**
     * Function to call at the creation of the view
     *
     * @author Hugo Berthomé
     */
    override fun onViewCreated() {
        connection.switchToSleepMode(true)
        lockScreen.enableShowWhenLock()
        view?.printHalo(model.getSizeOfCircle())
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
}