package com.example.spacexinfo.activity_helpers

import android.content.Intent
import android.net.Uri

class ImageActivityHelper {
        fun getIntentForDownloadImage(): Intent{
            val DownloadIntent: Intent = Intent().apply {
                action = Intent.ACTION_CREATE_DOCUMENT
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/jpeg"
            }
            return DownloadIntent
        }

        fun getOpenWithIntent(uri: Uri): Intent{
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return intent
        }
}