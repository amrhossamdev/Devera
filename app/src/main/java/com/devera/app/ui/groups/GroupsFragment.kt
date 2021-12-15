package com.devera.app.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.ui.groups.adapter.GroupAdapter
import com.devera.app.ui.groups.models.GroupModel
import com.devera.app.ui.groups.models.GroupResponse

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
        val data = ArrayList<GroupModel>()
        data.add(
            GroupModel(
                "C++",
                "",
            )
        )
        data.add(
            GroupModel(
                "JAVA",
                "",
            )
        )
        data.add(
            GroupModel(
                "C++",
                "",
            )
        )
        data.add(
            GroupModel(
                "C++",
                "",
            )
        )
        data.add(
            GroupModel(
                "C++",
                "",
            )
        )
        groupResponse = GroupResponse(data)
        initAdapter(view)
        return view
    }

    private fun initAdapter(view: View) {
        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        groupAdapter =
            GroupAdapter(requireContext(), groupResponse)
        recyclerView.adapter = groupAdapter
    }

}