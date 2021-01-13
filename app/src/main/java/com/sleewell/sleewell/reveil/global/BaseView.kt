package com.sleewell.sleewell.reveil.global

/**
 * Interface used for every view of the alarm as an extension
 *
 * @param T presenter that will be used by the the view
 * @author Romane Bézier
 */
interface BaseView<T> {

    /**
     * Set the presenter of the view
     *
     * @param presenter The presenter
     * @author Romane Bézier
     */
    fun setPresenter(presenter: T)
}