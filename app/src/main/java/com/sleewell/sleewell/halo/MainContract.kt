package com.sleewell.sleewell.halo

import com.sleewell.sleewell.mvp.Global.BasePresenter
import com.sleewell.sleewell.mvp.Global.BaseView

interface MainContract {
    interface Model {
        fun getSizeOfCircle() : Int
        fun degradesSizeOfCircle()
        fun upgradeSizeOfCircle()
        fun resetSizeOfCircle()
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun startProtocol()
        fun stopProtocol()
    }

    interface View : BaseView<Presenter> {
        fun printHalo(size: Int)
    }
}