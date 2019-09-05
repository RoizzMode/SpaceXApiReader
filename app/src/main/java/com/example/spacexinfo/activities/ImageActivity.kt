package com.example.spacexinfo.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.spacexinfo.R
import com.example.spacexinfo.data.SpaceXApplication
import com.example.spacexinfo.viewModels.ImageActivityViewModel
import com.example.spacexinfo.viewModels.ImageActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_image.*
import android.app.Activity
import androidx.core.view.isVisible
import com.example.spacexinfo.activity_helpers.ImageActivityHelper
import com.example.spacexinfo.contracts.ImageViewModel
import com.google.android.material.snackbar.Snackbar


class ImageActivity : AppCompatActivity() {

    private lateinit var viewModel: ImageViewModel
    private var bmp: Bitmap? = null
    private var name: String? = null
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        initViewModel()
        initImage()
        initButtons()
    }

    private fun initViewModel() {
        val helper = ImageActivityHelper()
        viewModel = ViewModelProviders.of(
            this,
            ImageActivityViewModelFactory((application as SpaceXApplication).spaceXModel)
        ).get(ImageActivityViewModel::class.java)
        viewModel.downloadParams.observe(this, Observer {
            val params = it.getEventOrNullIfHandled()
            if (params != null) {
                val intent = helper.getIntentForDownloadImage()
                startActivityForResult(intent, 1)
            }
        })
        viewModel.uri.observe(this, Observer {
            val intent = helper.getOpenWithIntent(it)
            startActivity(Intent.createChooser(intent, null))
        })
        viewModel.snackBarMessage.observe(this, Observer { showSnackbar(it.duration, it.message) })
        viewModel.imageViewVisibility.observe(this, Observer { setImageViewVisibility(it) })
        viewModel.failMessageVisibility.observe(this, Observer { setFailMessageVisibility(it) })
    }

    private fun initImage() {
        name = intent.getStringExtra(MISSION_NAME)
        uri = intent.getParcelableExtra(IMAGE) as Uri
        if (uri == null || name == null) {
            viewModel.uriOrNameWasntPassed()
            return
        }

        Glide
            .with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    rocketPhotoLarge.setImageBitmap(resource)
                    bmp = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun initButtons() {
        download_button.setOnClickListener {
            val localBmp = bmp
            if (localBmp != null)
                viewModel.downloadButtonClicked(localBmp)
        }
        open_with_button.setOnClickListener {
            val localBmp = bmp
            if (localBmp != null)
                viewModel.openWithButtonClicked(localBmp, name ?: "")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackbar(duration: Int, message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, duration).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val localData = data.data
                if (localData != null)
                    viewModel.fileCreated(localData)
                else
                    viewModel.fileCouldntCreate()
            }
            viewModel.fileCouldntCreate()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.viewStopped()
    }

    private fun setImageViewVisibility(visibility: Boolean) {
        rocketPhotoLarge.isVisible = visibility
    }

    private fun setFailMessageVisibility(visibility: Boolean) {
        fail_intent_message.isVisible = visibility
    }

    companion object {
        fun getStartIntent(activity: Activity, imageUri: Uri, missionName: String): Intent {
            val intent = Intent(activity, ImageActivity::class.java)
            intent.putExtra(IMAGE, imageUri)
            intent.putExtra(MISSION_NAME, missionName)
            return intent
        }

        private const val IMAGE = "image"
        private const val MISSION_NAME = "missionName"
    }
}
