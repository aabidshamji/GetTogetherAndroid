package com.example.aabid.gittogether.mapactivityadapter
/*
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aabid.gittogether.MainActivity
import com.example.aabid.gittogether.R
import com.example.aabid.gittogether.data.User
import com.example.aabid.gittogether.touch.TouchHelperAdapter
import kotlinx.android.synthetic.main.group_member_row_content.view.*
import java.util.*

class MapActivityAdapter : RecyclerView.Adapter<MapActivityAdapter.ViewHolder>, TouchHelperAdapter {


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
            R.layout.group_member_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return groupMembersItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val groupMembers = groupMembersItems[position]

        holder.tvName.text = groupMembers.name
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
        val tvName = groupMembersView.tvUserName
        val ivProfilePic = itemView.ivProfilePic
    }


    fun addGroupMembers(groupMembers: User) {
        groupMembersItems.add(0, groupMembers)
        notifyItemInserted(0)
    }

    override fun onDismissed(position: Int) {
        deleteGroupMembers(position)
    }

    override fun onUserMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(groupMembersItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}
        */
