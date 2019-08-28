package com.example.spacexinfo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spacexinfo.R
import com.example.spacexinfo.contracts.RecyclerClickListener
import com.example.spacexinfo.data.LaunchesInfo

class LaunchListAdapter(private val launchListList: List<LaunchesInfo>, private val clickListener: RecyclerClickListener): RecyclerView.Adapter<LaunchListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.launch_item, parent, false))
    }

    override fun getItemCount(): Int {
        return launchListList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launchListList[position]

        holder.flightNumber.text = holder.launchView.resources.getString(R.string.flight_number_first, launch.flightNumber)
        holder.missionName.text = launch.missionName
        holder.launchYear.text = launch.launchYear

        initItemClickListener(holder, position)
    }

    private fun initItemClickListener(holder: ViewHolder, position: Int) {
        holder.launchView.setOnClickListener {
            clickListener.itemClicked(position)
        }
    }

    class ViewHolder(val launchView: View) : RecyclerView.ViewHolder(launchView) {
        val flightNumber: TextView = launchView.findViewById(R.id.flight_number)
        val missionName: TextView = launchView.findViewById(R.id.mission_name)
        val launchYear: TextView = launchView.findViewById(R.id.launch_year)
    }
}