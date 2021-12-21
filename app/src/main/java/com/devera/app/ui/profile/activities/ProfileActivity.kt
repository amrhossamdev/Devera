package com.devera.app.ui.profile.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.network.ApiInterface
import com.devera.app.network.RetrofitInstance
import com.devera.app.ui.home.adapter.HomeAdapter
import com.devera.app.ui.home.models.BodyRequestsModel.ProfileBody
import com.devera.app.ui.home.models.HomeResponse
import com.devera.app.ui.profile.models.ProfileResponse
import com.devera.app.ui.signIn.activities.SignInActivity
import com.learnawy.app.storage.AppReferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileActivity : AppCompatActivity() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var profileRes: ProfileResponse
    private lateinit var homeResponse: HomeResponse

    private lateinit var loading: View


    var ctx = this;
    var hideMenuItem = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initToolbar()
        loading = findViewById(R.id.loading)

//        homeResponse = HomeResponse(data);
//        initAdapter()
        val mIntent = intent
        val profileId = mIntent.getIntExtra("id", -1)
        if (profileId != -1) {
            getUserFeed(profileId)
            hideMenuItem = true;
        } else {
            getUserFeed(AppReferences.getUserData(ctx)!!.id)

        }
    }


    private fun initToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }

    }

    private fun initAdapter() {
        loading = findViewById(R.id.loading)
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this@ProfileActivity)
        homeAdapter =
            HomeAdapter(this@ProfileActivity, profileRes)
        recyclerView.adapter = homeAdapter
    }

    private fun editProfile() {
        val profileImage: ImageView = findViewById(R.id.editProfile)
        val profileName: TextView = findViewById(R.id.nameTxt)
        val postsCount: TextView = findViewById(R.id.postsCount)

        profileImage.setOnClickListener {
            val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }
        if (AppReferences.getUserData(ctx)!!.id != profileRes.user.id) {
            profileImage.visibility = View.GONE
        }else{
            profileImage.visibility = View.VISIBLE
        }
        profileName.text = profileRes.user.fullName
        postsCount.text = profileRes.data.message.size.toString() + " Posts"
        title = profileRes.user.fullName
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val item = menu!!.findItem(R.id.logout)

        if (hideMenuItem) {
            item.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.logout) {
            AppReferences.setLoginState(this@ProfileActivity, false)
            val i = Intent(this@ProfileActivity, SignInActivity::class.java)
            startActivity(i)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUserFeed(id: Int) {
        loading.visibility = View.VISIBLE
        val retIn =
            RetrofitInstance.getRetrofitInstance(ctx).create(ApiInterface::class.java)
        val profileBody = ProfileBody(id)
        retIn.getProfile(profileBody).enqueue(object : Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                loading.visibility = View.GONE
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        profileRes = response.body()!!
                        if (response.body()!!.data.message.isNotEmpty()) {
                            initAdapter()

                        } else {
                            Toast.makeText(ctx, "No feed", Toast.LENGTH_SHORT).show()
                        }
                        editProfile()
                        loading.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(ctx, "No Feed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}