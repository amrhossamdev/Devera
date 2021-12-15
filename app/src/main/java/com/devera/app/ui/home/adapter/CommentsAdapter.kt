package com.devera.app.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.ui.home.models.CommentResponse
import de.hdodenhof.circleimageview.CircleImageView


class CommentsAdapter(var context: Context, var data: CommentResponse) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profileImage: CircleImageView = itemView.findViewById(R.id.profileImage)
        var userName: TextView = itemView.findViewById(R.id.userName)
        var commentDesc: TextView = itemView.findViewById(R.id.commentTxt)
        var time: TextView = itemView.findViewById(R.id.timeTxt)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName.text = data.comments[position].name
        holder.commentDesc.text = data.comments[position].desc
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = data.comments.size
}
