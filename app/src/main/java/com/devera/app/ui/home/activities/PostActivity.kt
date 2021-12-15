package com.devera.app.ui.home.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.ui.home.adapter.CommentsAdapter
import com.devera.app.ui.home.models.CommentModel
import com.devera.app.ui.home.models.CommentResponse

class PostActivity : AppCompatActivity() {

    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentsResponse: CommentResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        initToolbar()
        val data = ArrayList<CommentModel>()
        data.add(
            CommentModel(
                "Amr Hossam",
                "Lol xd !",
                "",
                "20 min",
            )
        )
        data.add(
            CommentModel(
                "Youssef",
                "A Static Website (sometimes called a flat or stationary page)",
                "",
                "50 min",
            )
        )
        data.add(
            CommentModel(
                "Abdelaziz",
                "1 2 3 Allah allah test test",
                "",
                "20 min",
            )
        )
        commentsResponse = CommentResponse(data);
        initAdapter()

    }

    private fun initAdapter() {
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this@PostActivity)
        commentsAdapter =
            CommentsAdapter(this@PostActivity, commentsResponse)
        recyclerView.adapter = commentsAdapter
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}