package com.sleewell.sleewell.mvp.Global

interface BaseView<T> {
    fun setPresenter(presenter : T)
}