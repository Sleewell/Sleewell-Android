package com.sleewell.sleewell.reveil.global

interface BaseView<T> {

    /**
     * Set the presenter of the view
     *
     * @param presenter The presenter
     */
    fun setPresenter(presenter: T)
}