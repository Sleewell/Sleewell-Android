package com.sleewell.sleewell.halo.Model

import android.content.Context
import com.sleewell.sleewell.halo.MainContract

class HaloModel(context: Context) : MainContract.Model {
    private var size: Int = 100

    override fun getSizeOfCircle(): Int {
        return size
    }

    override fun upgradeSizeOfCircle() {
        if (size < 1000)
            size = size + 50
    }

    override fun degradesSizeOfCircle() {
        if (size > 50)
            size = size - 50
    }
}