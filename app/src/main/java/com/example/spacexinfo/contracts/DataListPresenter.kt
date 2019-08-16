package com.example.spacexinfo.contracts

interface DataListPresenter: RecyclerScroller, RecyclerClickListener {
    fun attachView(currentView: DataListView)
    fun viewCreated()
    fun viewReturned()
    fun viewStopped()
}