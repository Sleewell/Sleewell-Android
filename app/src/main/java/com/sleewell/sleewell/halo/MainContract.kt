package com.sleewell.sleewell.halo

import android.app.Dialog
import android.graphics.ColorFilter
import com.sleewell.sleewell.mvp.Global.BasePresenter
import com.sleewell.sleewell.mvp.Global.BaseView

interface MainContract {
    interface Model {
        fun getSizeOfCircle() : Int
        fun getColorOfCircle() : ColorFilter
        fun degradesSizeOfCircle()
        fun upgradeSizeOfCircle()
        fun resetSizeOfCircle()
        fun openColorPicker(): Dialog
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun startProtocol()
        fun stopProtocol()
        fun openDialog()
    }

    interface View : BaseView<Presenter> {
        fun printHalo(size: Int)
        fun hideSystemUI()
        fun setColorHalo(color: ColorFilter)
    }
}