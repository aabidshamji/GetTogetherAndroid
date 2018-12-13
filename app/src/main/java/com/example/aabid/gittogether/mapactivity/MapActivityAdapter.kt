package com.example.aabid.gittogether.mapactivity

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aabid.gittogether.R
import com.example.aabid.gittogether.data.User
import kotlinx.android.synthetic.main.group_member_row_content.view.*
import java.util.*

class MapActivityAdapter : RecyclerView.Adapter<MapActivityAdapter.ViewHolder> {


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
        //TODO Add user to group and display
        /*Thread {
            AppDatabase.getInstance(
                context).groupMembersDao().deleteGroupMembers(groupMembersItems[adapterPosition])

            groupMembersItems.removeAt(adapterPosition)

            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()*/
    }

    inner class ViewHolder(groupMembersView: View) : RecyclerView.ViewHolder(groupMembersView)
    {
        val tvName = groupMembersView.tvUserName
    }


    fun addGroupMembers(newMember: User) {

        var mod = false

        for (g in groupMembersItems) {
            if (g.uid == newMember.uid) {
                g.uid = newMember.uid
                g.groups = newMember.groups
                g.latitude = newMember.latitude
                g.longitude = newMember.longitude
                g.name = newMember.name
                mod = true
                notifyDataSetChanged()
                break
            }
        }

        if (!mod) {
            groupMembersItems.add(0, newMember)
        }

        notifyItemInserted(0)

    }
}
