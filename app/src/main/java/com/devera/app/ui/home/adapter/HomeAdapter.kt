package com.devera.app.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.ui.home.activities.PostActivity
import com.devera.app.ui.home.models.HomeResponse
import org.w3c.dom.Text


class HomeAdapter(var context: Context, var datalist: HomeResponse) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var postImage: ImageView = itemView.findViewById(R.id.postImage)
        var desc: TextView = itemView.findViewById(R.id.postDescTxt)
        var userName: TextView = itemView.findViewById(R.id.userNameTxt)
        var upVoteBtn: ImageButton = itemView.findViewById(R.id.upVoteBtn)
        var downVoteBtn: ImageButton = itemView.findViewById(R.id.downVoteBtn)
        var typeComment: TextView = itemView.findViewById(R.id.typeAnswer_editText)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isUpVoted = false
        var isDownVoted = false
        if (datalist.subjects[position].reactId != null) {
            if (datalist.subjects[position].reactId == 1) {
                isUpVoted = true
            } else if (datalist.subjects[position].reactId == 0) {
                isDownVoted = true
            }
        }
        handleReact(isUpVoted, isDownVoted, holder)
        handleClickOnReact(isUpVoted, isDownVoted, holder, position)
        handleCommentOnClick(holder, position)

        holder.userName.text = datalist.subjects[position].name
        holder.desc.text = datalist.subjects[position].desc
        if (!datalist.subjects[position].hasImage) {
            holder.postImage.visibility = View.GONE
        } else {
            holder.postImage.visibility = View.VISIBLE
        }
    }

    private fun handleReact(isUpVoted: Boolean, isDownVoted: Boolean, holder: ViewHolder) {
        if (isUpVoted) {
            changeDrawables(
                holder.upVoteBtn,
                R.drawable.rounded_upvote,
                R.color.white
            )
        } else {
            changeDrawables(
                holder.upVoteBtn,
                R.drawable.round_corner_imagebtn,
                R.color.upvoteColor
            )
        }
        if (isDownVoted) {
            changeDrawables(
                holder.downVoteBtn,
                R.drawable.rounded_downvote,
                R.color.white
            )
        } else {
            changeDrawables(
                holder.downVoteBtn,
                R.drawable.round_corner_imagebtn,
                R.color.downVoteColor
            )
        }
    }

    private fun handleClickOnReact(
        isUpVoted: Boolean,
        isDownVoted: Boolean,
        holder: ViewHolder,
        position: Int
    ) {
        holder.upVoteBtn.setOnClickListener {
            if (isUpVoted) {
                // you will do unVote !
                datalist.subjects[position].reactId = null

            } else {
                // send request to upVote
                datalist.subjects[position].reactId = 1

            }
            notifyItemChanged(position)

        }
        holder.downVoteBtn.setOnClickListener {
            if (isDownVoted) {
                // send request to do unDownVote !
                datalist.subjects[position].reactId = null

            } else {
                // send request to downVote
                datalist.subjects[position].reactId = 0

            }
            notifyItemChanged(position)
        }
    }

    private fun changeDrawables(view: ImageButton, background: Int, tint: Int) {
        view.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                background
            )
        );
        view.setColorFilter(
            ContextCompat.getColor(context, tint),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun handleCommentOnClick(holder: ViewHolder, position: Int) {
        holder.typeComment.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = datalist.subjects.size
}
