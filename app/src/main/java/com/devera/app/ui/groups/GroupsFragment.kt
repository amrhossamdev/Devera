package com.devera.app.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.network.ApiInterface
import com.devera.app.network.RetrofitInstance
import com.devera.app.ui.groups.adapter.GroupAdapter
import com.devera.app.ui.groups.models.GroupResponse
import com.devera.app.ui.home.models.HomeBody
import com.learnawy.app.storage.AppReferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupsFragment : Fragment() {


    private lateinit var groupAdapter: GroupAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var groupResponse: GroupResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_groups, container, false)
        getGroups(view)
        return view
    }

    private fun getGroups(view: View) {
        val retIn =
            RetrofitInstance.getRetrofitInstance(requireContext()).create(ApiInterface::class.java)
        val homeBody = HomeBody(AppReferences.getUserData(requireContext())!!.id)
        retIn.getGroups().enqueue(object : Callback<GroupResponse> {
            override fun onFailure(call: Call<GroupResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<GroupResponse>,
                response: Response<GroupResponse>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        groupResponse = response.body()!!
                        initAdapter(view)
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun initAdapter(view: View) {
        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        groupAdapter =
            GroupAdapter(requireContext(), groupResponse)
        recyclerView.adapter = groupAdapter
    }

}