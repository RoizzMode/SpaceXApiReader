package com.example.spacexinfo.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.spacexinfo.R
import com.example.spacexinfo.activities.ImageActivity
import com.example.spacexinfo.analysers.DeviceSizeAnalyser
import com.example.spacexinfo.contracts.OneItemViewModel
import com.example.spacexinfo.data.OneLaunchInfo
import com.example.spacexinfo.data.SpaceXApplication
import com.example.spacexinfo.routers.ListLaunchesRouter
import com.example.spacexinfo.viewModels.OneLaunchViewModel
import com.example.spacexinfo.viewModels.OneLaunchViewModelFactory
import kotlinx.android.synthetic.main.one_launch_fragment.*

class OneLaunchFragment : Fragment() {

    private lateinit var viewModel: OneItemViewModel
    private var isLarge = false
    private var flightNumberShared = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        initToolbar()
        initViewModelAndObservers()

        return inflater.inflate(R.layout.one_launch_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var flightNumber = arguments?.getInt(ListLaunchesRouter.FLIGHT_NUMBER)
        val sharedPrefs = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        if (sharedPrefs?.contains(FLIGHT_NUMBER) == true && flightNumber == null)
            flightNumber = sharedPrefs.getInt(FLIGHT_NUMBER, 1)
        viewModel.viewCreated(flightNumber ?: 1)
        initButton()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        isLarge = DeviceSizeAnalyser(activity?.applicationContext ?: throw NullPointerException()).isLarge()
        if (!isLarge) {
            (activity as AppCompatActivity).supportActionBar?.title = "Launch detail info"
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }

    private fun initViewModelAndObservers() {
        viewModel = ViewModelProviders.of(
            this,
            OneLaunchViewModelFactory((activity?.application as SpaceXApplication).spaceXModel, isLarge)
        ).get(OneLaunchViewModel::class.java)
        viewModel.oneLaunchInfo.observe(viewLifecycleOwner, Observer { showInfo(it)
        flightNumberShared = it.flightNumber.toInt()
        })
        viewModel.oneLaunchPhoto.observe(viewLifecycleOwner, Observer { showPhoto(it) })
        viewModel.photoVisibility.observe(viewLifecycleOwner, Observer { setPhotoVisibility(it) })
        viewModel.infoVisibility.observe(viewLifecycleOwner, Observer { setInfoVisibility(it) })
        viewModel.progressBarVisibility.observe(viewLifecycleOwner, Observer { setProgressBarVisibility(it) })
        viewModel.retryButtonAndMessageVisibility.observe(
            viewLifecycleOwner,
            Observer { setRetryButtonAndMessageVisibility(it) })
        viewModel.noPhotoMessageVisibility.observe(viewLifecycleOwner, Observer { setNoPhotoMessageVisibility(it) })
        viewModel.failMessageVisibility.observe(viewLifecycleOwner, Observer { setFailMessageVisibility(it) })
        viewModel.magnifierVisibility.observe(viewLifecycleOwner, Observer { setMagnifierVisibility(it) })
        viewModel.changeScreenIndicator.observe(viewLifecycleOwner, Observer {
            if (it.getContentIfNotHandled() != null)
                changeScreen()
        })
    }

    private fun showInfo(launch: OneLaunchInfo) {
        flight_number.text = getString(R.string.flight_number_text, launch.flightNumber)
        mission_name.text = getString(R.string.mission_name_text, launch.missionName)
        launch_year.text = getString(R.string.launch_year_text, launch.launchYear)
        rocket_name.text = getString(R.string.rocket_name_text, launch.rocketName)
        rocket_type.text = getString(R.string.rocket_type_text, launch.rocketType)
        cores.text = getString(R.string.cores_text, launch.coreSerial, launch.flight, launch.wasReused)
        block.text = getString(R.string.block_text, launch.blocks)
        payloads.text = getString(
            R.string.payloads_text,
            launch.payloadId,
            launch.payloadType,
            launch.nationality,
            launch.manufacturer
        )
    }

    private fun showPhoto(uri: Uri) {
        Glide
            .with(this)
            .load(uri)
            .into(rocketPhoto)
    }

    private fun setPhotoVisibility(visibility: Boolean) {
        rocketPhoto.isVisible = visibility
    }

    private fun setNoPhotoMessageVisibility(visibility: Boolean) {
        no_photo_message.isVisible = visibility
    }

    private fun setInfoVisibility(visibility: Boolean) {
        upperCard.isVisible = visibility
        lowerCard.isVisible = visibility
    }

    private fun setProgressBarVisibility(visibility: Boolean) {
        oneItemProgressBar.isVisible = visibility
    }

    override fun onStop() {
        super.onStop()
        val sharedNumber = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        sharedNumber?.edit()?.putInt(FLIGHT_NUMBER, flightNumberShared)?.apply() ?: throw NullPointerException()
        viewModel.viewStopped()
    }

    private fun setRetryButtonAndMessageVisibility(visibility: Boolean) {
        retryButton.isVisible = visibility
        failMessage.isVisible = visibility
    }

    private fun initButton() {
        retryButton.setOnClickListener {
            viewModel.retryButtonClicked()
        }
    }

    private fun setFailMessageVisibility(visibility: Boolean) {
        activity?.findViewById<AppCompatTextView>(R.id.fail_message)?.isVisible = visibility
    }

    private fun setMagnifierVisibility(visibility: Boolean) {
        magnifier.isVisible = visibility
    }

    private fun changeScreen(){
        val intent = Intent(activity, ImageActivity::class.java)
    }

    companion object{
        private const val APP_PREFERENCES = "sharedNumber"
        private const val FLIGHT_NUMBER = "flightNumber"
    }
}