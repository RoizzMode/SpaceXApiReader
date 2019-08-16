package com.example.spacexinfo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.spacexinfo.R
import com.example.spacexinfo.analysers.DeviceSizeAnalyser
import com.example.spacexinfo.consts.PassDataConsts
import com.example.spacexinfo.contracts.OneItemPresenter
import com.example.spacexinfo.contracts.OneItemView
import com.example.spacexinfo.data.OneLaunchInfo
import com.example.spacexinfo.data.SpaceXApplication
import com.example.spacexinfo.presenters.OneLaunchPresenter
import kotlinx.android.synthetic.main.one_launch_fragment.*

class OneLaunchFragment: Fragment(), OneItemView {

    private lateinit var presenter: OneItemPresenter
    private var isLarge = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        isLarge = DeviceSizeAnalyser(activity?.applicationContext ?: throw NullPointerException()).isLarge()
        if (!isLarge) {
            (activity as AppCompatActivity).supportActionBar?.title = "Launch detail info"
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        return inflater.inflate(R.layout.one_launch_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val flightNumber = arguments?.getInt(PassDataConsts.flightNumber)
        presenter = OneLaunchPresenter((activity?.application as SpaceXApplication).spaceXModel, isLarge)
        presenter.attachView(this)
        presenter.viewCreated(flightNumber ?: 1)
        initButton()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showInfo(launch: OneLaunchInfo) {
        flight_number.text = getString(R.string.flight_number_text, launch.flightNumber)
        mission_name.text = getString(R.string.mission_name_text, launch.missionName)
        launch_year.text = getString(R.string.launch_year_text, launch.launchYear)
        rocket_name.text = getString(R.string.rocket_name_text, launch.rocketName)
        rocket_type.text = getString(R.string.rocket_type_text, launch.rocketType)
        cores.text = getString(R.string.cores_text, launch.coreSerial, launch.flight, launch.wasReused)
        block.text = getString(R.string.block_text, launch.blocks)
        payloads.text = getString(R.string.payloads_text, launch.payloadId, launch.payloadType, launch.nationality, launch.manufacturer)
    }

    override fun setInfoVisibility(visibility: Boolean) {
        upperCard.isVisible = visibility
        lowerCard.isVisible = visibility
    }

    override fun setProgressBarVisibility(visibility: Boolean) {
        oneItemProgressBar.isVisible = visibility
    }

    override fun onStop() {
        super.onStop()
        presenter.viewStopped()
    }

    override fun setFailMessageAndRetryButtonVisibility(visibility: Boolean) {
        retryButton.isVisible = visibility
        failMessage.isVisible = visibility
    }

    private fun initButton(){
        retryButton.setOnClickListener {
            presenter.retryButtonClicked()
        }
    }
}