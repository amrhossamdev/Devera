package com.devera.app.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.network.ApiInterface
import com.devera.app.network.RetrofitInstance
import com.devera.app.ui.home.activities.PostActivity
import com.devera.app.ui.home.models.BodyRequestsModel.UpVotingBody
import com.devera.app.ui.home.models.VoteActionsModel
import com.devera.app.ui.profile.activities.ProfileActivity
import com.devera.app.ui.profile.models.ProfileResponse
import com.learnawy.app.storage.AppReferences
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class HomeAdapter(var context: Context, var datalist: ProfileResponse) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var postImage: ImageView = itemView.findViewById(R.id.postImage)
        var profileImage: CircleImageView = itemView.findViewById(R.id.profileImage)
        var desc: TextView = itemView.findViewById(R.id.postDescTxt)
        var userName: TextView = itemView.findViewById(R.id.userNameTxt)
        var upVoteBtn: ImageButton = itemView.findViewById(R.id.upVoteBtn)
        var downVoteBtn: ImageButton = itemView.findViewById(R.id.downVoteBtn)
        var typeComment: TextView = itemView.findViewById(R.id.typeAnswer_editText)
        var time: TextView = itemView.findViewById(R.id.timeTxt)
        var groupName: TextView = itemView.findViewById(R.id.groupNameTxt)
        var downVoteCounter: TextView = itemView.findViewById(R.id.downVoteCount)
        var upVoteCounter: TextView = itemView.findViewById(R.id.upVoteCount)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isUpVoted = false
        var isDownVoted = false
        var userReact = datalist.data.message[position].userReact
        if (userReact != -1) {
            if (userReact == 1) {
                isUpVoted = true
            } else if (userReact == 0) {
                isDownVoted = true
            }
        }
        Log.e("VOTE DATA", userReact.toString())

        handleReact(isUpVoted, isDownVoted, holder)
        handleClickOnReact(isUpVoted, isDownVoted, holder, position)
        handleCommentOnClick(holder, position)

        handleOnProfileClick(holder)

        val timeStamp = datalist.data.message[position].post.createdAt
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
        val bookingTime: Instant = Instant.parse(timeStamp)
        val finalTime: String = bookingTime.atZone(ZoneId.systemDefault()).format(timeFormatter)

        holder.postImage.visibility = View.GONE
        //setting data
        holder.time.text = finalTime
        holder.userName.text = datalist.data.message[position].post.userName
        holder.desc.text = datalist.data.message[position].post.postAbstract
        holder.groupName.text = "in " + getGroupName(datalist.data.message[position].post.groupId)
        holder.downVoteCounter.text =
            "- " + datalist.data.message[position].downVoteCounter.toString() + " downV"
        holder.upVoteCounter.text =
            "+ " + datalist.data.message[position].upVoteCounter.toString() + " upVote"

//        if (!datalist.subjects[position].hasImage) {
//        } else {
//            holder.postImage.visibility = View.VISIBLE
//        }
    }

    private fun getGroupName(id: Int): String {
        when (id) {
            1 -> {
                return "C++";
            }
            2 -> {
                return "Java"
            }
            3 -> {
                return "Android development";
            }
            4 -> {
                return "Web development";
            }
            5 -> {
                return "Cyber security"
            }
        }
        return "";
    }

    private fun handleOnProfileClick(holder: ViewHolder) {
        holder.profileImage.setOnClickListener {
            val i = Intent(context, ProfileActivity::class.java)
            i.putExtra("id", datalist.data.message[holder.adapterPosition].post.userId)
            context.startActivity(i)
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
                sendUpVoteRequest(position, isUpVoted, isDownVoted)
            } else {
                sendUpVoteRequest(position, isUpVoted, isDownVoted)
            }
            notifyItemChanged(position)
        }
        holder.downVoteBtn.setOnClickListener {
            if (isDownVoted) {
                // send request to do unDownVote !
                sendDownVote(position, isDownVoted, isUpVoted)

            } else {
                // send request to downVote
                sendDownVote(position, isDownVoted, isUpVoted)
            }
            notifyItemChanged(position)
        }
    }

    private fun sendUpVoteRequest(pos: Int, isUpVoted: Boolean, isDownVoted: Boolean) {
        val retIn = RetrofitInstance.getRetrofitInstance(context).create(ApiInterface::class.java)
        val upVote =
            UpVotingBody(
                datalist.data.message[pos].post.id,
                AppReferences.getUserData(context)!!.id
            )

        retIn.upVote(upVote).enqueue(object : Callback<VoteActionsModel> {
            override fun onFailure(call: Call<VoteActionsModel>, t: Throwable) {
                Toast.makeText(
                    context,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<VoteActionsModel>,
                response: Response<VoteActionsModel>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        if (isUpVoted) {
                            datalist.data.message[pos].userReact = -1
                            datalist.data.message[pos].upVoteCounter -= 1;
                        } else {
                            // new edit
                            if (isDownVoted) {
                                datalist.data.message[pos].downVoteCounter -= 1;
                            }
                            datalist.data.message[pos].userReact = 1
                            datalist.data.message[pos].upVoteCounter += 1;
                        }
                        notifyItemChanged(pos)
                    }
                }
            }
        })
    }

    private fun sendDownVote(pos: Int, isDownVoted: Boolean, isUpVoted: Boolean) {
        val retIn = RetrofitInstance.getRetrofitInstance(context).create(ApiInterface::class.java)
        val downVote =
            UpVotingBody(
                datalist.data.message[pos].post.id,
                AppReferences.getUserData(context)!!.id
            )

        retIn.downVote(downVote).enqueue(object : Callback<VoteActionsModel> {
            override fun onFailure(call: Call<VoteActionsModel>, t: Throwable) {
                Toast.makeText(
                    context,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<VoteActionsModel>,
                response: Response<VoteActionsModel>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        if (isDownVoted) {
                            datalist.data.message[pos].userReact = -1
                            datalist.data.message[pos].downVoteCounter -= 1;

                        } else {
                            // new edit
                            if (isUpVoted) {
                                datalist.data.message[pos].upVoteCounter -= 1;
                            }
                            datalist.data.message[pos].userReact = 0
                            datalist.data.message[pos].downVoteCounter += 1;

                        }
                        notifyItemChanged(pos)
                    }
                }
            }
        })
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
            intent.putExtra("post", datalist.data.message[holder.adapterPosition])
            context.startActivity(intent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = datalist.data.message.size
}
