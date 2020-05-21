package com.sleewell.sleewell.halo.Model

import android.content.Context
import android.widget.Toast
import com.sleewell.sleewell.halo.MainContract
import kotlin.coroutines.coroutineContext

class HaloModel(context: Context) : MainContract.Model {
    private var size: Int = 10

    override fun getSizeOfCircle(): Int {
        return size
    }

    override fun upgradeSizeOfCircle() {
        if (size < 1000)
            size = size + 3
    }

    override fun degradesSizeOfCircle() {
        if (size > 10 && size - 2 > 10)
            size = size - 2
    }

    override fun resetSizeOfCircle() {
        size = 10
    }
}