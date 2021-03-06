package com.example.aabid.gittogether.groupadapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aabid.gittogether.R
import com.example.aabid.gittogether.data.Group
import kotlinx.android.synthetic.main.group_row_content.view.*

class HomeActivityAdapter : RecyclerView.Adapter<HomeActivityAdapter.ViewHolder> {


    private var groupList = mutableListOf<Group>()

    val context : Context

    constructor(context: Context, groups: List<Group>) : super() {
        this.context = context
        this.groupList.addAll(groups)
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.group_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groupList[position]

        holder.tvGroupName.text = group.name
        holder.tvUserNum.text = group.members.size.toString()
        holder.tvGroupID.text = group.uid
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val tvGroupName = itemView.tvGroupName!!
        val tvUserNum = itemView.tvUserNum!!
        val tvGroupID = itemView.tvGroupID!!
    }


    fun addGroup(group: Group) {

        var mod = false

        for (g in groupList) {
            if (g.uid == group.uid) {
                g.uid = group.uid
                g.founder = group.founder
                g.members = group.members
                g.latitude = group.latitude
                g.longitude = group.longitude
                g.name = group.name
                mod = true
                notifyDataSetChanged()
                break
            }
        }

        if (!mod) {
            groupList.add(0, group)
        }

        notifyItemInserted(0)
    }
}