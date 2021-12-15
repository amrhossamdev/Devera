package com.devera.app.ui.groups.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.ui.groups.activites.GroupPostActivity
import com.devera.app.ui.groups.models.GroupResponse


class GroupAdapter(var context: Context, var data: GroupResponse) :
    RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var groupImage: ImageView = itemView.findViewById(R.id.groupImage)
        var groupTitle: TextView = itemView.findViewById(R.id.groupName)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.groupTitle.text = data.groups[position].name
        holder.itemView.setOnClickListener{
            val intent = Intent(context,GroupPostActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = data.groups.size
}
