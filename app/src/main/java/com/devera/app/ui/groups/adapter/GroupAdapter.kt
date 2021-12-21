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
        holder.groupTitle.text = data.data[position].groupName
        setGroupImage(holder, position)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, GroupPostActivity::class.java)
            intent.putExtra("id", data.data[holder.adapterPosition].id)
            intent.putExtra("name", data.data[holder.adapterPosition].groupName)
            context.startActivity(intent)
        }
    }

    private fun setGroupImage(holder: ViewHolder, pos: Int) {
        when (data.data[pos].id) {
            1 -> {
                holder.groupImage.setImageResource(R.drawable.cplusplus)
            }
            2 -> {
                holder.groupImage.setImageResource(R.drawable.java)
            }
            3 -> {
                holder.groupImage.setImageResource(R.drawable.android)
            }
            4 -> {
                holder.groupImage.setImageResource(R.drawable.web)
            }
            5 -> {
                holder.groupImage.setImageResource(R.drawable.security)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = data.data.size
}
