package com.devera.app.ui.groups.activites

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.network.ApiInterface
import com.devera.app.network.RetrofitInstance
import com.devera.app.ui.BaseModel.BaseResponse
import com.devera.app.ui.groups.models.Body.AddPostBody
import com.devera.app.ui.groups.models.Body.GroupBody
import com.devera.app.ui.home.adapter.HomeAdapter
import com.devera.app.ui.profile.models.ProfileResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.learnawy.app.storage.AppReferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupPostActivity : AppCompatActivity() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private var groupId: Int = 0
    private var groupName: String = ""

    private lateinit var groupRes: ProfileResponse
    private lateinit var noPosts: View

    private lateinit var scrollView: NestedScrollView
    private lateinit var addPost: FloatingActionButton
    private var oldScrollYPostion = 0
    private lateinit var dialog: Dialog

    var ctx = this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        initToolbar()
        initViews()
        handleScroll()
        groupId = ((intent.getSerializableExtra("id") as? Int)!!)
        groupName = ((intent.getSerializableExtra("name") as? String)!!)
        noPosts = findViewById(R.id.noPosts)
        title = groupName

        Log.e("userId", AppReferences.getUserData(ctx)!!.id.toString())

        getGroupFeed()
        addPost()
    }

    private fun initToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
    }

    private fun initViews() {
        scrollView = findViewById(R.id.scrollView)
        addPost = findViewById(R.id.floatingActionButton)
    }

    private fun handleScroll() {
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            if (scrollView.scrollY > oldScrollYPostion) {
                addPost.hide()
            } else if (scrollView.scrollY < oldScrollYPostion || scrollView.scrollY <= 0) {
                addPost.show()
            }
            oldScrollYPostion = scrollView.scrollY
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog(context: Context, msg: String?) {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val window: Window = dialog.window!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.setContentView(R.layout.add_post_dialog)
        val name = dialog.findViewById(R.id.userNameTxt) as TextView
        name.text = AppReferences.getUserData(ctx)!!.userName
        val postDesc = dialog.findViewById(R.id.postDesc) as TextInputEditText
        var send = dialog.findViewById<ImageButton>(R.id.updatePassword)
        send.setOnClickListener {
            val desc = postDesc.text.toString()
            if (desc.isNotEmpty()) addPost(desc)

        }
        dialog.show()
    }

    private fun addPost() {
        addPost.setOnClickListener {
            showDialog(this@GroupPostActivity, "lol")
        }
    }

    fun <T> reverseList(list: List<T>): List<T> {
        return list.indices.map { i: Int -> list[list.size - 1 - i] }
    }

    private fun getGroupFeed() {
        val retIn =
            RetrofitInstance.getRetrofitInstance(ctx).create(ApiInterface::class.java)
        val groupBody = GroupBody(groupId, AppReferences.getUserData(ctx)!!.id)
        retIn.getGroupPage(groupBody).enqueue(object : Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        groupRes = response.body()!!
                        var reverse = reverseList(groupRes.data.message)
                        groupRes.data.message = reverse
                        if (response.body()!!.data.message.isNotEmpty()) {
                            initAdapter()
                            noPosts.visibility = View.GONE

                        } else {
                            noPosts.visibility = View.VISIBLE
                        }
                    }
                } else {
                    Toast.makeText(ctx, "No Feed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun addPost(postDesc: String) {
        val retIn =
            RetrofitInstance.getRetrofitInstance(ctx).create(ApiInterface::class.java)
        val addPost = AddPostBody(
            groupId,
            postDesc,
            AppReferences.getUserData(ctx)!!.id,
        )
        retIn.addPost(addPost).enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        getGroupFeed()
                        dialog.dismiss()
                    }
                } else {
                    Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initAdapter() {
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this@GroupPostActivity)
        homeAdapter =
            HomeAdapter(this@GroupPostActivity, groupRes)
        recyclerView.adapter = homeAdapter
    }
}