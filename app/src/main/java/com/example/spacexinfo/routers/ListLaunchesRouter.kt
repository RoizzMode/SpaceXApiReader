package com.example.spacexinfo.routers

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.spacexinfo.R
import com.example.spacexinfo.fragments.OneLaunchFragment

class ListLaunchesRouter(private val currentFragment: Fragment) {

    fun goToNext(num: Int){
        val oneLaunchFragment = OneLaunchFragment()
        val arguments = Bundle().apply {
            putInt(FLIGHT_NUMBER, num)
        }
        oneLaunchFragment.arguments = arguments
        val fragmentTransaction = currentFragment.fragmentManager?.beginTransaction() ?: throw NullPointerException()
        fragmentTransaction
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            .replace(R.id.container, oneLaunchFragment)
            .addToBackStack(oneLaunchFragment::class.java.name)
            .commit()
    }

    companion object{
        const val FLIGHT_NUMBER = "flightNumber"
    }
}