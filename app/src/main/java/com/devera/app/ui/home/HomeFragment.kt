package com.devera.app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.network.ApiInterface
import com.devera.app.network.RetrofitInstance
import com.devera.app.ui.home.adapter.HomeAdapter
import com.devera.app.ui.home.models.HomeBody
import com.devera.app.ui.profile.models.ProfileResponse
import com.learnawy.app.storage.AppReferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeResponse: ProfileResponse
    private lateinit var loading: View
    private lateinit var viw: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        viw = view
        loading = view.findViewById(R.id.loading)
        Log.e("USERID", AppReferences.getUserData(requireContext())!!.id.toString())
        return view
    }

    override fun onStart() {
        super.onStart()
        getFeed(viw)
    }

    private fun getFeed(view: View) {
        loading.visibility = View.VISIBLE
        val retIn =
            RetrofitInstance.getRetrofitInstance(requireContext()).create(ApiInterface::class.java)
        val homeBody = HomeBody(AppReferences.getUserData(requireContext())!!.id)
        retIn.getFeed(
            homeBody
        ).enqueue(object : Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                loading.visibility = View.GONE
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        if (response.body()!!.data.message.isNotEmpty()) {
                            homeResponse = response.body()!!
                            var reverse = reverseList(homeResponse.data.message)
                            homeResponse.data.message = reverse
                            initAdapter(view)
                        }
                        loading.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
    fun <T> reverseList(list: List<T>): List<T> {
        return list.indices.map { i: Int -> list[list.size - 1 - i] }
    }
    private fun initAdapter(view: View) {
        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        homeAdapter =
            HomeAdapter(requireContext(), homeResponse)
        recyclerView.adapter = homeAdapter
    }


}