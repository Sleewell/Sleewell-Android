package com.sleewell.sleewell.mvp.protocol

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface ProtocolContract {

    interface Presenter : BasePresenter {
        fun onViewCreated()
    }

    interface View : BaseView<Presenter>
}