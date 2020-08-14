package com.sleewell.sleewell.mvp.global

/**
 * Interface used for every view as an extension
 *
 * @param T Presenter that will be used by the the view
 * @author Hugo Berthomé
 */
interface BaseView<T> {
    fun setPresenter(presenter : T)
}