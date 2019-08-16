package com.example.spacexinfo.data

import com.example.spacexinfo.contracts.DataLoadListener
import com.example.spacexinfo.pojos.LaunchListPOJO
import com.example.spacexinfo.network.LaunchLoader
import com.example.spacexinfo.network.OneItemLoader
import com.example.spacexinfo.pojos.OneItemPOJO
import java.lang.ref.WeakReference

class SpaceXModel {

    private val oneLaunchLoader = OneItemLoader(this)
    private val launchLoader = LaunchLoader(this)
    private var launchList = arrayListOf<LaunchListPOJO>()
    private val dataListeners = arrayListOf<WeakReference<DataLoadListener>>()
    private val oneItemListeners = arrayListOf<WeakReference<DataLoadListener>>()
    private val dataStartedLoadingListeners = arrayListOf<WeakReference<DataLoadListener>>()
    private val loadFailListeners = arrayListOf<WeakReference<DataLoadListener>>()
    private lateinit var oneLaunch: OneItemPOJO
    var isLast = false

    fun loadLaunches(){
        launchLoader.loadData(launchList.size)
    }

    fun setOnDataLoadListener(listener: DataLoadListener){
        dataListeners.add(WeakReference(listener))
    }

    fun removeOnDataLoadListener(listener: DataLoadListener){
        for (i in 0..dataListeners.lastIndex){
            if (dataListeners[i].get() == listener) {
                dataListeners.removeAt(i)
                break
            }
        }
    }

    fun setOnOneItemLoadListener(listener: DataLoadListener){
        oneItemListeners.add(WeakReference(listener))
    }

    fun removeOnOneItemLoadListener(listener: DataLoadListener){
        for (i in 0..oneItemListeners.lastIndex){
            if (oneItemListeners[i].get() == listener) {
                oneItemListeners.removeAt(i)
                break
            }
        }
    }

    fun setOnDataStartedLoadingListener(listener: DataLoadListener){
        dataStartedLoadingListeners.add(WeakReference(listener))
    }

    fun removeOnDataStartedLoadingListener(listener: DataLoadListener){
        for (i in 0..dataStartedLoadingListeners.lastIndex){
            if (dataStartedLoadingListeners[i].get() == listener) {
                dataStartedLoadingListeners.removeAt(i)
                break
            }
        }
    }

    fun setOnLoadFailListener(listener: DataLoadListener){
        loadFailListeners.add(WeakReference(listener))
    }

    fun removeOnLoadFailListener(listener: DataLoadListener){
        for (i in 0..loadFailListeners.lastIndex){
            if (loadFailListeners[i].get() == listener) {
                loadFailListeners.removeAt(i)
                break
            }
        }
    }

    fun getLaunches(): List<LaunchesInfo>{
        val launchesInfo = arrayListOf<LaunchesInfo>()
        for (i in 0..launchList.lastIndex){
            launchesInfo.add(LaunchesInfo(launchList[i].missionName, launchList[i].flightNumber.toString(), launchList[i].launchYear.toString()))
        }
        return launchesInfo
    }

    fun findNumber(position: Int): Int{
        return launchList[position].flightNumber
    }

    fun loadOneLaunch(flightNumber: Int){
        oneLaunchLoader.loadData(flightNumber)
        for (i in 0..dataStartedLoadingListeners.lastIndex){
            if (dataStartedLoadingListeners[i].get() != null)
                dataStartedLoadingListeners[i].get()?.onDataLoaded()
        }
    }

    fun launchListLoaded(){
        launchList.addAll(launchLoader.getData())
        isLast = launchLoader.isLast
        for (i in 0..dataListeners.lastIndex){
            if (dataListeners[i].get() != null)
                dataListeners[i].get()?.onDataLoaded()
        }
    }

    fun oneItemLoaded(){
        oneLaunch = oneLaunchLoader.getData()
        for (i in 0..oneItemListeners.lastIndex){
            if (oneItemListeners[i].get() != null)
                oneItemListeners[i].get()?.onDataLoaded()
        }
    }

    fun getOneLaunch(): OneLaunchInfo {
        return OneLaunchInfo(oneLaunch.flightNumber.toString(),
            oneLaunch.missionName,
            oneLaunch.launchYear.toString(),
            oneLaunch.rocket.firstStage.cores[0].coreSerial,
            oneLaunch.rocket.firstStage.cores[0].flight.toString(),
            oneLaunch.rocket.firstStage.cores[0].reused.toString(),
            oneLaunch.rocket.rocketName,
            oneLaunch.rocket.rocketType,
            oneLaunch.rocket.secondStage.block.toString(),
            oneLaunch.rocket.secondStage.payloads[0].payloadId,
            oneLaunch.rocket.secondStage.payloads[0].payloadType,
            oneLaunch.rocket.secondStage.payloads[0].nationality,
            oneLaunch.rocket.secondStage.payloads[0].manufacturer)
    }

    fun oneItemLoadFailed(){
        for (i in 0..loadFailListeners.lastIndex){
            if (loadFailListeners[i].get() != null)
                loadFailListeners[i].get()?.onDataLoaded()
        }
    }
}