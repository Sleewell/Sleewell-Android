package com.sleewell.sleewell.halo.Presenter

import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import com.sleewell.sleewell.halo.MainContract
import com.sleewell.sleewell.halo.Model.HaloModel

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

    override fun onViewCreated() {
        view?.printHalo(model.getSizeOfCircle())
    }

    override fun onDestroy() {
        view = null
    }

    override fun startProtocol() {
        nbrBreath = 10
        model.resetSizeOfCircle()
        timer.start()
    }

    override fun stopProtocol() {
        timer.cancel()
    }
}