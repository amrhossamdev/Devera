package com.devera.app.ui.home.activities

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.network.ApiInterface
import com.devera.app.network.RetrofitInstance
import com.devera.app.ui.BaseModel.BaseResponse
import com.devera.app.ui.home.adapter.CommentsAdapter
import com.devera.app.ui.home.models.*
import com.devera.app.ui.home.models.BodyRequestsModel.AddCommentBody
import com.devera.app.ui.home.models.BodyRequestsModel.CommentsBody
import com.devera.app.ui.home.models.BodyRequestsModel.UpVotingBody
import com.devera.app.ui.profile.models.Message
import com.learnawy.app.storage.AppReferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class PostActivity : AppCompatActivity() {

    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentsResponse: CommentModel
    private lateinit var post: Message
    private lateinit var loading: View
    private lateinit var commentView: View
    private lateinit var commentEditText: EditText
    private lateinit var commentText: TextView
    private lateinit var upVoteBtn: ImageButton
    private lateinit var downVoteBtn: ImageButton
    private lateinit var upCnt: TextView
    private lateinit var downCnt: TextView


    var context = this

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        initToolbar()
        initData()
        initView()

    }

    private fun initAdapter() {
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this@PostActivity)
        commentsAdapter =
            CommentsAdapter(this@PostActivity, commentsResponse)
        recyclerView.adapter = commentsAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        commentText = findViewById(R.id.postsCount)
        val sendComment = findViewById<ImageButton>(R.id.updatePassword)
        commentEditText = findViewById(R.id.commentEditText)

        sendComment.setOnClickListener {
            Log.e("amr", "lol")
            val comment = commentEditText.text.toString()
            if (comment.isNotEmpty()) {
                addComment(comment)
            } else {
                Toast.makeText(context, getString(R.string.please_type_comment), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initData() {
        loading = findViewById(R.id.loading)
        commentView = findViewById(R.id.noCommentView)
        var groupName = findViewById<TextView>(R.id.groupNameTxt)
        var userName = findViewById<TextView>(R.id.userNameTxt)
        var postDesc = findViewById<TextView>(R.id.postDescTxt)
        var time = findViewById<TextView>(R.id.timeTxt)

        post = (intent.getSerializableExtra("post") as? com.devera.app.ui.profile.models.Message)!!
        getComments(post.post.id)

        //initPost
        groupName.text = "in " + getGroupName(post.post.groupId)
        userName.text = post.post.userName
        postDesc.text = post.post.postAbstract
        //Parse server time

        val timeStamp = post.post.createdAt
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
        val bookingTime: Instant = Instant.parse(timeStamp)
        val finalTime: String = bookingTime.atZone(ZoneId.systemDefault()).format(timeFormatter)
        time.text = finalTime
        initReact()
    }

    fun initReact() {
        upVoteBtn = findViewById(R.id.upVoteBtn)
        downVoteBtn = findViewById(R.id.downVoteBtn)
        upCnt = findViewById(R.id.upVoteCount)
        downCnt = findViewById(R.id.downVoteCount)

        var isUpVoted = false
        var isDownVoted = false
        val userReact = post.userReact
        if (userReact != -1) {
            if (userReact == 1) {
                isUpVoted = true
            } else if (userReact == 0) {
                isDownVoted = true
            }
        }
        upCnt.text =
            "+ " + post.upVoteCounter.toString() + " downV"
        downCnt.text =
            "- " + post.downVoteCounter.toString() + " upVote"
        handleReact(isUpVoted, isDownVoted)
        handleClickOnReact(isUpVoted, isDownVoted)

    }

    private fun handleClickOnReact(
        isUpVoted: Boolean,
        isDownVoted: Boolean
    ) {
        upVoteBtn.setOnClickListener {
            if (isUpVoted) {
                // you will do unVote !
                sendUpVoteRequest(isUpVoted, isDownVoted)
            } else {
                sendUpVoteRequest(isUpVoted, isDownVoted)
            }
            initReact()
        }
        downVoteBtn.setOnClickListener {
            if (isDownVoted) {
                // send request to do unDownVote !
                sendDownVote(isDownVoted, isUpVoted)

            } else {
                // send request to downVote
                sendDownVote(isDownVoted, isUpVoted)
            }
            initReact()
        }
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

    private fun addComment(comment: String) {
        val retIn =
            RetrofitInstance.getRetrofitInstance(context).create(ApiInterface::class.java)
        val commentBody = AddCommentBody(
            comment, post.post.id, post.post.userId, AppReferences.getUserData(context)!!.userName
        )
        retIn.addComment(
            commentBody
        ).enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                loading.visibility = View.GONE
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                loading.visibility = View.GONE
                if (response.body() != null) {
                    if (response.body()!!.status) {

                        commentEditText.text.clear()
                        getComments(post.post.id)
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun initToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
        title = "Post"
    }

    private fun getComments(id: Int) {
        loading.visibility = View.VISIBLE
        val retIn =
            RetrofitInstance.getRetrofitInstance(context).create(ApiInterface::class.java)
        val commentBody = CommentsBody(id)
        retIn.getComments(
            commentBody
        ).enqueue(object : Callback<CommentModel> {
            override fun onFailure(call: Call<CommentModel>, t: Throwable) {
                loading.visibility = View.GONE
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CommentModel>,
                response: Response<CommentModel>
            ) {
                loading.visibility = View.GONE

                if (response.body() != null) {
                    if (response.body()!!.status) {
                        if (response.body()!!.data.isNotEmpty()) {
                            commentView.visibility = View.GONE
                            commentsResponse = response.body()!!
                            //reverse comment list
                            val reverse = reverseList(commentsResponse.data)
                            commentsResponse.data = reverse
                            initAdapter()
                            commentText.text = commentsResponse.data.size.toString() + " Comment"

                        } else {
                            commentText.text = "0 Comment"
                            commentView.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    fun <T> reverseList(list: List<T>): List<T> {
        return list.indices.map { i: Int -> list[list.size - 1 - i] }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendUpVoteRequest(isUpVoted: Boolean, isDownVoted: Boolean) {
        val retIn = RetrofitInstance.getRetrofitInstance(context).create(ApiInterface::class.java)
        val upVote =
            UpVotingBody(post.post.id, AppReferences.getUserData(context)!!.id)

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
                            post.userReact = -1
                            post.upVoteCounter -= 1;
                        } else {
                            // new edit
                            if (isDownVoted) {
                                post.downVoteCounter -= 1;
                            }
                            post.userReact = 1
                            post.upVoteCounter += 1;
                        }
                        initReact()
                    }
                }
            }
        })
    }

    private fun sendDownVote(isDownVoted: Boolean, isUpVoted: Boolean) {
        val retIn = RetrofitInstance.getRetrofitInstance(context).create(ApiInterface::class.java)
        val downVote =
            UpVotingBody(post.post.id, AppReferences.getUserData(context)!!.id)

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
                            post.userReact = -1
                            post.downVoteCounter -= 1;

                        } else {
                            // new edit
                            if (isUpVoted) {
                                post.upVoteCounter -= 1;
                            }
                            post.userReact = 0
                            post.downVoteCounter += 1;

                        }
                        initReact()
                    }
                }
            }
        })
    }


    private fun handleReact(
        isUpVoted: Boolean,
        isDownVoted: Boolean,
    ) {
        if (isUpVoted) {
            changeDrawables(
                upVoteBtn,
                R.drawable.rounded_upvote,
                R.color.white
            )
        } else {
            changeDrawables(
                upVoteBtn,
                R.drawable.round_corner_imagebtn,
                R.color.upvoteColor
            )
        }
        if (isDownVoted) {
            changeDrawables(
                downVoteBtn,
                R.drawable.rounded_downvote,
                R.color.white
            )
        } else {
            changeDrawables(
                downVoteBtn,
                R.drawable.round_corner_imagebtn,
                R.color.downVoteColor
            )
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

}