package com.example.aabid.gettogether.mapgroupmembers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aabid.gettogether.R
import com.example.aabid.gettogether.data.User
import com.example.aabid.gettogether.touch.TouchHelperAdapter
import java.util.*

class GroupMembersAdapter : RecyclerView.Adapter<GroupMembersAdapter.ViewHolder>, TouchHelperAdapter {


    var groupMembersItems = mutableListOf<User>()

    val context : Context

    constructor(context: Context, groupMembersList: List<User>) : super() {
        this.context = context
        this.groupMembersItems.addAll(groupMembersList)
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.groupMembers_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return groupMembersItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val groupMembers = groupMembersItems[position]

        holder.tvName.text = groupMembers.groupMembersName

        holder.btnDelete.setOnClickListener {
            deleteGroupMembers(holder.adapterPosition)
        }
    }

    private fun deleteGroupMembers(adapterPosition: Int) {
        Thread {
            AppDatabase.getInstance(
                    context).groupMembersDao().deleteGroupMembers(groupMembersItems[adapterPosition])

            groupMembersItems.removeAt(adapterPosition)

            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

    inner class ViewHolder(groupMembersView: View) : RecyclerView.ViewHolder(groupMembersView)
    {
        val tvName = groupMembersView.tvName
        val btnDelete = groupMembersView.btnDelete
    }


    fun addGroupMembers(groupMembers: GroupMembers) {
        groupMembersItems.add(0, groupMembers)
        notifyItemInserted(0)
    }

    override fun onDismissed(position: Int) {
        deleteGroupMembers(position)
    }

    override fun onGroupMembersMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(groupMembersItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}