package com.sleewell.sleewell.mvp.global

/**
 * Interface used for every presenter as an extension
 * @author Hugo Berthomé
 */
interface BasePresenter {
    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    fun onDestroy()
}