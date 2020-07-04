package com.sleewell.sleewell.halo.Presenter

import android.content.Context
import android.media.Image
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import com.sleewell.sleewell.R
import com.sleewell.sleewell.halo.MainContract
import com.sleewell.sleewell.halo.Model.HaloModel

/**
 * This class is the presenter for the halo and call each method of the model for control the halo
 *
 * @param view view of the halo
 * @param context current context of the app
 * @author gabin warnier de wailly
 */
class HaloPresenter(view: MainContract.View, context: Context) : MainContract.Presenter {

    private var view: MainContract.View? = view
    private var model: MainContract.Model = HaloModel(context)
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
     * This method setup and display the halo
     *
     * @author gabin warnier de wailly
     */
    override fun onViewCreated() {
        view?.printHalo(model.getSizeOfCircle())
    }

    /**
     * This method is the destructor of the class
     *
     * @author gabin warnier de wailly
     */
    override fun onDestroy() {
        view = null
    }

    /**
     * this method start the protocol with a specific number of repetition
     *
     * @param Int the number of repetition for the halo
     * @author gabin warnier de wailly
     */
    override fun startProtocol(number :Int) {
        timer.cancel()
        nbrBreath = number
        model.resetSizeOfCircle()
        timer.start()
    }

    /**
     * This method stop the current protocol
     *
     * @author gabin warnier de wailly
     */
    override fun stopProtocol() {
        timer.cancel()
    }

    /**
     * This method open the colorPicker directly on the view
     *
     * @author gabin warnier de wailly
     */
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