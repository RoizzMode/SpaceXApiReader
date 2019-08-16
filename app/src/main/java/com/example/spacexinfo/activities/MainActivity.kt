package com.example.spacexinfo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.spacexinfo.R
import com.example.spacexinfo.analysers.DeviceSizeAnalyser
import com.example.spacexinfo.fragments.ListLaunchesFragment
import com.example.spacexinfo.fragments.OneLaunchFragment
import kotlinx.android.synthetic.main.phone_layout.*
import kotlinx.android.synthetic.main.tablet_layout.*

class MainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private lateinit var listLaunchesFragment: ListLaunchesFragment
    private lateinit var oneLaunchFragment: OneLaunchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deviceSizeAnalyser = DeviceSizeAnalyser(this)
        if (deviceSizeAnalyser.isLarge()) {
            setContentView(R.layout.tablet_layout)
            setSupportActionBar(toolbar2)
            if (savedInstanceState == null)
                initLargeScreen()
        }
        else {
            setContentView(R.layout.phone_layout)
            setSupportActionBar(toolbar)
            if (savedInstanceState == null)
                initSmallScreen()
        }
    }

    private fun initLargeScreen(){
        listLaunchesFragment = ListLaunchesFragment()
        oneLaunchFragment = OneLaunchFragment()
        replaceFragment(R.id.container_left, listLaunchesFragment)
        replaceFragment(R.id.container_right, oneLaunchFragment)
    }

    private fun initSmallScreen(){
        val listLaunchesFragment = ListLaunchesFragment()
        replaceFragment(R.id.container, listLaunchesFragment)
    }

    private fun replaceFragment(container: Int, fragment: Fragment){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(container, fragment)
        fragmentTransaction.commit()
    }
}
