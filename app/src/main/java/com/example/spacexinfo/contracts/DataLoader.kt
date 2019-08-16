package com.example.spacexinfo.contracts

import com.example.spacexinfo.pojos.LaunchListPOJO

interface DataLoader {
    fun loadData(from: Int)
    fun getData(): List<LaunchListPOJO>
}