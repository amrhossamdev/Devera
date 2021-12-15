package com.devera.app.ui.profile.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.ui.home.adapter.HomeAdapter
import com.devera.app.ui.home.models.HomeModel
import com.devera.app.ui.home.models.HomeResponse

class ProfileActivity : AppCompatActivity() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeResponse: HomeResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initToolbar()
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
        title = "Amr Hossam"
    }

    private fun initAdapter() {
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this@ProfileActivity)
        homeAdapter =
            HomeAdapter(this@ProfileActivity, homeResponse)
        recyclerView.adapter = homeAdapter
    }
}