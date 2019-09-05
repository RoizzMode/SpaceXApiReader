package com.example.spacexinfo.data

import android.content.*
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.spacexinfo.R
import com.example.spacexinfo.bases.AppDatabase
import com.example.spacexinfo.bases.DetailEntity
import com.example.spacexinfo.bases.LaunchesEntity
import com.example.spacexinfo.contracts.DataLoadListener
import com.example.spacexinfo.network.LaunchRepository
import com.example.spacexinfo.network.OneItemRepository
import com.example.spacexinfo.pojos.*
import java.lang.ref.WeakReference
import com.example.spacexinfo.contracts.ImageDownloadListener
import java.io.File


class SpaceXModel(private val context: Context, appDatabase: AppDatabase) {

    private val oneLaunchLoader = OneItemRepository(this)
    private val launchLoader = LaunchRepository(this)

    val resources: Resources? = context.resources

    private var launchList = arrayListOf<LaunchListPOJO>()
    private var oneLaunch: OneItemPOJO? = null
    lateinit var bmp: Bitmap

    private val dataListeners = arrayListOf<WeakReference<DataLoadListener<List<LaunchesInfo>>>>()
    private val oneItemListeners = arrayListOf<WeakReference<DataLoadListener<OneLaunchInfo?>>>()
    val imageListeners = arrayListOf<WeakReference<DataLoadListener<Uri?>>>()
    val imageDownloadListeners = arrayListOf<WeakReference<ImageDownloadListener>>()

    private val launchesDAO = appDatabase.launchesDao()
    private val detailsDAO = appDatabase.detailsDao()

    var flightNumber = 1
    var isLast = false
    var isError = false
    var startedLoading = false
    var isLoading = false

    fun loadLaunches() {
        if (!isLoading) {
            isLoading = true
            launchLoader.loadData(launchList.size)
        }
    }

    fun getLaunches(): List<LaunchesInfo> {
        val launchesInfo = arrayListOf<LaunchesInfo>()
        for (i in 0..launchList.lastIndex) {
            launchesInfo.add(
                LaunchesInfo(
                    launchList[i].missionName,
                    launchList[i].flightNumber.toString(),
                    launchList[i].launchYear.toString()
                )
            )
        }
        return launchesInfo
    }

    fun findNumber(position: Int): Int {
        return launchList[position].flightNumber
    }

    fun loadOneLaunch() {
        startedLoading = true
        oneLaunchLoader.loadData(flightNumber)
        notifyListeners(oneItemListeners, getOneLaunch())
    }

    fun launchListLoaded() {
        isLoading = false
        val list = launchLoader.getData()
        launchList.addAll(list)
        val thread = Thread {
            for (i in 0..list.lastIndex) {
                Log.v("fn", list[i].flightNumber.toString())
                launchesDAO.insert(
                    LaunchesEntity(
                        flightNumber = list[i].flightNumber,
                        missionName = list[i].missionName,
                        launchYear = list[i].launchYear
                    )
                )
            }
        }
        thread.start()
        if (list.isEmpty())
            isLast = true
        isError = false
        notifyListeners(dataListeners, getLaunches())
    }

    fun oneItemLoaded() {
        startedLoading = false
        oneLaunch = oneLaunchLoader.getData()
        isError = false
        val item = oneLaunch
        if (item != null) {
            val thread = Thread {
                val idItem = detailsDAO.getDetail(item.flightNumber.toString())
                if (idItem.isNotEmpty()) {
                    val id = idItem[0].id
                    val detailForUpdate = DetailEntity(
                        id = id,
                        flightNumber = item.flightNumber,
                        missionName = item.missionName,
                        coreSerial = item.rocket.firstStage.cores[0].coreSerial,
                        flight = item.rocket.firstStage.cores[0].flight,
                        wasReused = item.rocket.firstStage.cores[0].reused,
                        rocketName = item.rocket.rocketName,
                        rocketType = item.rocket.rocketType,
                        blocks = item.rocket.secondStage.block,
                        payloadId = item.rocket.secondStage.payloads[0].payloadId,
                        payloadType = item.rocket.secondStage.payloads[0].payloadType,
                        nationality = item.rocket.secondStage.payloads[0].nationality,
                        manufacturer = item.rocket.secondStage.payloads[0].manufacturer,
                        photoURL = when (item.links.flickrImages.isEmpty()) {
                            true -> null
                            false -> item.links.flickrImages[0]
                        }
                    )
                    detailsDAO.update(detailForUpdate)
                } else {
                    val detailForInsert = DetailEntity(
                        flightNumber = item.flightNumber,
                        missionName = item.missionName,
                        coreSerial = item.rocket.firstStage.cores[0].coreSerial,
                        flight = item.rocket.firstStage.cores[0].flight,
                        wasReused = item.rocket.firstStage.cores[0].reused,
                        rocketName = item.rocket.rocketName,
                        rocketType = item.rocket.rocketType,
                        blocks = item.rocket.secondStage.block,
                        payloadId = item.rocket.secondStage.payloads[0].payloadId,
                        payloadType = item.rocket.secondStage.payloads[0].payloadType,
                        nationality = item.rocket.secondStage.payloads[0].nationality,
                        manufacturer = item.rocket.secondStage.payloads[0].manufacturer,
                        photoURL = when (item.links.flickrImages.isEmpty()) {
                            true -> null
                            false -> item.links.flickrImages[0]
                        }
                    )
                    detailsDAO.insert(detailForInsert)
                }
            }
            thread.start()
        }
        notifyListeners(oneItemListeners, getOneLaunch())
    }

    private fun getOneLaunch(): OneLaunchInfo? {
        val item = oneLaunch
        if (item != null)
            return OneLaunchInfo(
                item.flightNumber.toString(),
                item.missionName,
                item.launchYear.toString(),
                item.rocket.firstStage.cores[0].coreSerial ?: context.getString(R.string.TBA),
                when (item.rocket.firstStage.cores[0].flight) {
                    null -> context.getString(R.string.TBA)
                    else -> item.rocket.firstStage.cores[0].flight.toString()
                },
                when (item.rocket.firstStage.cores[0].reused) {
                    true -> context.getString(R.string.yes)
                    false -> context.getString(R.string.no)
                    null -> context.getString(R.string.TBA)
                },
                item.rocket.rocketName ?: context.getString(R.string.TBA),
                item.rocket.rocketType ?: context.getString(R.string.TBA),
                when (item.rocket.secondStage.block) {
                    null -> context.getString(R.string.TBA)
                    else -> item.rocket.secondStage.block.toString()
                },
                item.rocket.secondStage.payloads[0].payloadId ?: context.getString(R.string.TBA),
                item.rocket.secondStage.payloads[0].payloadType ?: context.getString(R.string.TBA),
                item.rocket.secondStage.payloads[0].nationality ?: context.getString(R.string.TBA),
                item.rocket.secondStage.payloads[0].manufacturer ?: context.getString(R.string.TBA)
            )
        return null
    }

    fun getPhoto(): Uri? {
        val item = oneLaunch
        if (item != null && item.links.flickrImages.isEmpty())
            return null
        if (item != null && item.links.flickrImages[0] != null)
            return Uri.parse(item.links.flickrImages[0])
        return null
    }

    fun loadFailed() {
        isLoading = false
        isError = true
        notifyListeners(dataListeners, getLaunches())
    }

    private fun <T> notifyListeners(listeners: List<WeakReference<DataLoadListener<T>>>, data: T) {
        for (i in 0..listeners.lastIndex) {
            if (listeners[i].get() != null) {
                listeners[i].get()?.onDataLoadingEvent(data, startedLoading, isError)
            }
        }
    }

    private fun getLaunchesFromDB() {
        val thread = Thread {
            val list = launchesDAO.getAllLaunches()
            for (i in 0..list.lastIndex) {
                launchList.add(LaunchListPOJO(list[i].flightNumber, list[i].missionName, list[i].launchYear))
            }
            val uiHandler = Handler(Looper.getMainLooper())
            uiHandler.post {
                if (list.isEmpty())
                    loadLaunches()
                else
                    notifyListeners(dataListeners, getLaunches())
            }
        }
        thread.start()
    }

    fun initModel() {
        clearCache(context.cacheDir)
        getLaunchesFromDB()
    }

    private fun getOneItemFromDB() {
        val thread = Thread {
            val item = detailsDAO.getDetail(flightNumber.toString())
            val itemMainInfo = launchesDAO.getLaunchMainInfo(flightNumber.toString())
            if (item.isEmpty())
                oneLaunch = null
            else
                oneLaunch = OneItemPOJO(
                    item[0].flightNumber,
                    itemMainInfo[0].missionName,
                    itemMainInfo[0].launchYear,
                    RocketPOJO(
                        item[0].rocketName,
                        item[0].rocketType,
                        FirstStagePOJO(
                            listOf(
                                CoresPOJO(
                                    item[0].coreSerial,
                                    item[0].flight,
                                    item[0].wasReused
                                )
                            )
                        ),
                        SecondStagePOJO(
                            item[0].blocks,
                            listOf(
                                PayloadsPOJO(
                                    item[0].payloadId,
                                    item[0].payloadType,
                                    item[0].nationality,
                                    item[0].manufacturer
                                )
                            )
                        )
                    ),
                    LinkPOJO(
                        listOf(
                            when (item[0].photoURL) {
                                null -> null
                                else -> item[0].photoURL
                            }
                        )
                    )
                )
            val uiHandler = Handler(Looper.getMainLooper())
            uiHandler.post { notifyListeners(oneItemListeners, getOneLaunch()) }
        }
        thread.start()
    }

    fun loadOneFailed() {
        startedLoading = false
        isError = true
        getOneItemFromDB()
    }

    fun setOnDataLoadListener(listener: DataLoadListener<List<LaunchesInfo>>) {
        dataListeners.add(WeakReference(listener))
    }

    fun removeOnDataLoadListener(listener: DataLoadListener<List<LaunchesInfo>>) {
        for (i in 0..dataListeners.lastIndex) {
            if (dataListeners[i].get() == listener) {
                dataListeners.removeAt(i)
                break
            }
        }
    }

    fun setOnOneItemLoadListener(listener: DataLoadListener<OneLaunchInfo?>) {
        oneItemListeners.add(WeakReference(listener))
    }

    fun removeOnOneItemLoadListener(listener: DataLoadListener<OneLaunchInfo?>) {
        for (i in 0..oneItemListeners.lastIndex) {
            if (oneItemListeners[i].get() == listener) {
                oneItemListeners.removeAt(i)
                break
            }
        }
    }

    fun setImageListener(listener: DataLoadListener<Uri?>?) {
        if (listener != null)
            imageListeners.add(WeakReference(listener))
    }

    fun removeImageListener(listener: DataLoadListener<Uri?>?) {
        for (i in 0..imageListeners.lastIndex) {
            if (imageListeners[i].get() == listener) {
                imageListeners.removeAt(i)
                break
            }
        }
    }

    fun setImageDownloadListener(listener: ImageDownloadListener?) {
        if (listener != null)
            imageDownloadListeners.add(WeakReference(listener))
    }

    fun removeImageDownloadListener(listener: ImageDownloadListener?) {
        for (i in 0..imageDownloadListeners.lastIndex) {
            if (imageDownloadListeners[i].get() == listener) {
                imageDownloadListeners.removeAt(i)
                break
            }
        }
    }

    fun downloadImage(uri: Uri) {
        ImageDownloaderAndOpener(this).downloadBitmapToUri(context.contentResolver, uri, bmp)
    }

    fun notifyErrorCase(isLoading: Boolean, isError: Boolean) {
        for (i in 0..imageDownloadListeners.lastIndex) {
            if (imageDownloadListeners[i].get() != null) {
                imageDownloadListeners[i].get()?.onStateChanged(isLoading, isError)
            }
        }
    }

    fun createFileInCacheAndGetUri(bmp: Bitmap, name: String) {
        ImageDownloaderAndOpener(this).createFileInCacheAndGetUri(bmp, name, context)
    }

    fun notifySuccessCase(uri: Uri){
        notifyListeners(imageListeners, uri)
    }

    private fun clearCache(dir: File){
        val files = dir.listFiles() ?: return
        for (i in 0..files.lastIndex){
            files[i].delete()
        }
    }

    companion object {
        const val fileProviderAuthority = "com.example.spacexinfo.fileprovider"
    }
}