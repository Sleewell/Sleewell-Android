package com.sleewell.sleewell.mvp.global

/**
 * Interface used for every view as an extension
 *
 * @param T Presenter that will be used by the the view
 * @author Hugo Berthomé
 */
interface BaseView<T> {
    /**
     * Set the presenter inside the class
     *
     * @param presenter
     * @author Hugo Berthomé
     */
    fun setPresenter(presenter : T)
}