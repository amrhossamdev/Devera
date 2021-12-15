package com.devera.app.ui.groups.activites

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.ui.home.adapter.HomeAdapter
import com.devera.app.ui.home.models.HomeModel
import com.devera.app.ui.home.models.HomeResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupPostActivity : AppCompatActivity() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeResponse: HomeResponse
    private lateinit var scrollView: NestedScrollView
    private lateinit var addPost: FloatingActionButton
    private var oldScrollYPostion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        initToolbar()
        initViews()
        handleScroll()
        val data = ArrayList<HomeModel>()
        data.add(
            HomeModel(
                "Amr Hossam",
                "A Static Website (sometimes called a flat or stationary page) is displayed in a web browser exactly as it is stored. It contains web pages with fixed content coded in HTML and stored on a web server. It does not change",
                "Amr Hossam",
                true, 1
            )
        )
        data.add(
            HomeModel(
                "Youssef",
                "A Static Website (sometimes called a flat or stationary page) is displayed in a web browser exactly as it is stored. It contains web pages with fixed content coded in HTML and stored on a web server. It does not change",
                "Amr Hossam",
                false, 0
            )
        )
        data.add(
            HomeModel(
                "thba7ooo", "It's a great app !",
                "Amr Hossam", false, null
            )
        )
        data.add(
            HomeModel(
                "Amr", "تيست تيست واحد اتنين تلاته الله الله الله",
                "3b3azeez", false, 0
            )
        )
        data.add(
            HomeModel(
                "Amr Hossam",
                "A Static Website (sometimes called a flat or stationary page) is displayed in a web browser exactly as it is stored. It contains web pages with fixed content coded in HTML and stored on a web server. It does not change",
                "Abdelkader",
                true, 0
            )
        )
        homeResponse = HomeResponse(data);
        initAdapter()
    }

    private fun initToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
        title = "C++"
    }

    private fun initViews(){
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

    private fun initAdapter() {
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this@GroupPostActivity)
        homeAdapter =
            HomeAdapter(this@GroupPostActivity, homeResponse)
        recyclerView.adapter = homeAdapter
    }
}