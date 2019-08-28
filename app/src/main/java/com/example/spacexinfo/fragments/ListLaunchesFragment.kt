package com.example.spacexinfo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spacexinfo.R
import com.example.spacexinfo.adapters.LaunchListAdapter
import com.example.spacexinfo.analysers.DeviceSizeAnalyser
import com.example.spacexinfo.consts.ScrollConsts
import com.example.spacexinfo.contracts.DataListViewModel
import com.example.spacexinfo.data.LaunchesInfo
import com.example.spacexinfo.data.SpaceXApplication
import com.example.spacexinfo.viewModels.ListLaunchesViewModel
import com.example.spacexinfo.routers.ListLaunchesRouter
import com.example.spacexinfo.viewModels.ListLaunchesViewModelFactory
import kotlinx.android.synthetic.main.list_launches_fragment.*

class ListLaunchesFragment : Fragment() {

    private lateinit var viewModel: DataListViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private val listItems = arrayListOf<LaunchesInfo>()
    private var isLarge = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        initToolbar()
        initViewModelAndObservers()

        return inflater.inflate(R.layout.list_launches_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        viewModel.viewCreated()
        initScroller()
        initButton()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    override fun onStop() {
        super.onStop()
        viewModel.viewStopped()
    }

    private fun initToolbar(){
        isLarge = DeviceSizeAnalyser(activity?.applicationContext ?: throw NullPointerException()).isLarge()
        if (!isLarge) {
            (activity as AppCompatActivity).supportActionBar?.title = "List of SpaceX launches"
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }

    private fun initViewModelAndObservers(){
        viewModel = ViewModelProviders.of(
            this,
            ListLaunchesViewModelFactory((activity?.application as SpaceXApplication).spaceXModel, isLarge)
        ).get(ListLaunchesViewModel::class.java)

        viewModel.listLaunches.observe(viewLifecycleOwner, Observer { showItems(it) })
        viewModel.recyclerVisibility.observe(viewLifecycleOwner, Observer { setRecyclerVisibility(it) })
        viewModel.progressBarVisibility.observe(viewLifecycleOwner, Observer { setProgressBarVisibility(it) })
        viewModel.failProgressVisibility.observe(viewLifecycleOwner, Observer { setFailProgressVisibility(it) })
        viewModel.failMessageVisibility.observe(viewLifecycleOwner, Observer { setFailMessageVisibility(it) })
        viewModel.retryButtonVisibility.observe(viewLifecycleOwner, Observer { setRetryButtonVisibility(it) })

        viewModel.changeScreenWatcher.observe(viewLifecycleOwner, Observer {
            val num = it?.getContentIfNotHandled()
            if (num != null)
                changeScreen(num)
        })
    }

    private fun initList() {
        layoutManager = LinearLayoutManager(activity)
        launches_list.layoutManager = layoutManager
        launches_list.adapter = LaunchListAdapter(listItems, viewModel)
    }

    private fun initScroller() {
        launches_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItems = layoutManager.childCount
                val totalItems = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItems + firstVisibleItem) >= totalItems && firstVisibleItem >= 0 && totalItems >= ScrollConsts.PAGE_SIZE) {
                    viewModel.listScrolled()
                }
            }
        })
    }

    private fun showItems(list: List<LaunchesInfo>) {
        listItems.clear()
        listItems.addAll(list)
        launches_list.adapter?.notifyDataSetChanged() ?: throw NullPointerException()
    }

    private fun changeScreen(num: Int) {
        val listLaunchesRouter = ListLaunchesRouter(this)
        listLaunchesRouter.goToNext(num)
    }

    private fun setProgressBarVisibility(visibility: Boolean) {
        progressBar.isVisible = visibility
    }

    private fun setFailMessageVisibility(visibility: Boolean) {
        activity?.findViewById<AppCompatTextView>(R.id.fail_message)?.isVisible = visibility
    }

    private fun setRecyclerVisibility(visibility: Boolean) {
        launches_list.isVisible = visibility
    }

    private fun initButton() {
        retry_button.setOnClickListener {
            viewModel.retryButtonClicked()
        }
    }

    private fun setFailProgressVisibility(visibility: Boolean) {
        progressBarFail.isVisible = visibility
    }

    private fun setRetryButtonVisibility(visibility: Boolean) {
        retry_button.isVisible = visibility
    }
}