package com.example.aabid.gittogether.groupadapter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.aabid.gittogether.R
import com.example.aabid.gittogether.data.Group
import com.example.aabid.gittogether.data.User
import com.example.aabid.gittogether.touch.TouchHelperAdapter
import kotlinx.android.synthetic.main.group_member_row_content.view.*
import kotlinx.android.synthetic.main.group_row_content.view.*
import java.util.*

class HomeActivityAdapter : RecyclerView.Adapter<HomeActivityAdapter.ViewHolder> {


    var groupList = mutableListOf<Group>()

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
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val tvGroupName = itemView.tvGroupName
        val tvUserNum = itemView.tvUserNum
    }


    fun addGroup(group: Group) {
        groupList.add(0, group)
        notifyItemInserted(0)
    }

    fun addFullGroup(groupListNew: MutableList<Group>) {
        groupList = groupListNew
        notifyItemInserted(0)
    }

}