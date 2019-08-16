package com.example.spacexinfo.routers

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.spacexinfo.R
import com.example.spacexinfo.consts.PassDataConsts
import com.example.spacexinfo.fragments.OneLaunchFragment

class ListLaunchesRouter(private val currentFragment: Fragment) {

    fun goToNext(flightNumber: Int){
        val oneLaunchFragment = OneLaunchFragment()
        val arguments = Bundle().apply {
            putInt(PassDataConsts.flightNumber, flightNumber)
        }
        oneLaunchFragment.arguments = arguments

        val fragmentTransaction = currentFragment.fragmentManager?.beginTransaction() ?: throw NullPointerException()
        fragmentTransaction
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, oneLaunchFragment)
            .addToBackStack(oneLaunchFragment::class.java.name)
            .commit()
    }
}