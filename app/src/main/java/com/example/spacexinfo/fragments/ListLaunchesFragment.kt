package com.example.spacexinfo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spacexinfo.R
import com.example.spacexinfo.adapters.LaunchListAdapter
import com.example.spacexinfo.analysers.DeviceSizeAnalyser
import com.example.spacexinfo.consts.ScrollConsts
import com.example.spacexinfo.contracts.DataListPresenter
import com.example.spacexinfo.contracts.DataListView
import com.example.spacexinfo.data.LaunchesInfo
import com.example.spacexinfo.data.SpaceXApplication
import com.example.spacexinfo.presenters.ListLaunchesPresenter
import com.example.spacexinfo.routers.ListLaunchesRouter
import kotlinx.android.synthetic.main.list_launches_fragment.*

class ListLaunchesFragment : Fragment(), DataListView {

    private lateinit var presenter: DataListPresenter
    private lateinit var layoutManager: LinearLayoutManager
    private val listItems = arrayListOf<LaunchesInfo>()
    private var isLarge = false
    private var isLoading = false
    private var isLast = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        isLarge = DeviceSizeAnalyser(activity?.applicationContext ?: throw NullPointerException()).isLarge()
        if (!isLarge) {
            (activity as AppCompatActivity).supportActionBar?.title = "List of SpaceX launches"
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        }
        return inflater.inflate(R.layout.list_launches_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = ListLaunchesPresenter((activity?.application as SpaceXApplication).spaceXModel, isLarge)
        presenter.attachView(this)
        initList()
        initScroller()
        if (layoutManager.itemCount == 0) {
            presenter.viewCreated()
        } else
            presenter.viewReturned()
    }

    override fun onPause() {
        super.onPause()
        presenter.viewStopped()
    }

    private fun initList() {
        layoutManager = LinearLayoutManager(activity)
        launches_list.layoutManager = layoutManager
        launches_list.adapter = LaunchListAdapter(listItems, presenter)
    }

    private fun initScroller() {
        launches_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItems = layoutManager.childCount
                val totalItems = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItems + firstVisibleItem) >= totalItems && firstVisibleItem >= 0 && totalItems >= ScrollConsts.PAGE_SIZE) {
                    presenter.listScrolled()
                }
            }
        })
    }

    override fun showItems(launchListList: List<LaunchesInfo>) {
        listItems.clear()
        listItems.addAll(launchListList)
        isLoading = false
        progressBar.isVisible = false
        launches_list.adapter?.notifyDataSetChanged() ?: throw NullPointerException()
    }

    override fun changeScreen(flightNumber: Int) {
        val listLaunchesRouter = ListLaunchesRouter(this)
        listLaunchesRouter.goToNext(flightNumber)
    }

    override fun lastItemLoaded() {
        isLast = true
    }

    override fun setProgressBarVisibility(visibility: Boolean) {
        progressBar.isVisible = visibility
    }
}