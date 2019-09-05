package com.example.spacexinfo.data

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImageDownloaderAndOpener(private val model: SpaceXModel) {
    private var isLoading = false
    private var isError = false

    fun downloadBitmapToUri(contentResolver: ContentResolver, uri: Uri, bmp: Bitmap) {
        val thread = Thread {
            val handler = Handler(Looper.getMainLooper())
            val fileDesc = contentResolver.openFileDescriptor(uri, "w")
            val fos = FileOutputStream(fileDesc?.fileDescriptor)
            val bst = ByteArrayOutputStream()
            try {
                loadOutputStreamAndChangeStates(bst, fos, handler, bmp)
            } catch (e: InterruptedException) {
                notifyErrorCase(handler)
            } finally {
                fos.close()
            }
        }
        thread.start()
    }

    private fun loadOutputStreamAndChangeStates(bst: ByteArrayOutputStream, fos: FileOutputStream, handler: Handler, bmp: Bitmap){
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bst)
        val bytes = bst.toByteArray()
        isLoading = true
        isError = false
        handler.post { model.notifyErrorCase(isLoading, isError) }
        fos.write(bytes)
        isLoading = false
        handler.post { model.notifyErrorCase(isLoading, isError) }
    }

    private fun notifyErrorCase(handler:Handler){
        isLoading = false
        isError = true
        handler.post { model.notifyErrorCase(isLoading, isError)}
    }

    fun createFileInCacheAndGetUri(bmp: Bitmap, name: String, context: Context){
        val thread = Thread {
            val v = context.cacheDir
            val image = createFile(v, name)
            compressImage(image, bmp)
            val u = FileProvider.getUriForFile(context, SpaceXModel.fileProviderAuthority, image)
            val uiHandler = Handler(Looper.getMainLooper())
            uiHandler.post { model.notifySuccessCase(u) }
        }
        thread.start()
    }

    private fun createFile(v: File, name: String): File{
        val image = File(v, name.replace("\\s+".toRegex(), "") + ".jpg")
        if (image.exists())
            image.delete()
        image.parentFile?.mkdirs()
        image.createNewFile()
        return image
    }

    private fun compressImage(image: File, bmp: Bitmap){
        val foS = FileOutputStream(image)
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, foS)
        foS.flush()
        foS.close()
    }
}