package com.devera.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devera.app.R
import com.devera.app.ui.home.adapter.HomeAdapter
import com.devera.app.ui.home.models.HomeModel
import com.devera.app.ui.home.models.HomeResponse

class HomeFragment : Fragment() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeResponse: HomeResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
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
        initAdapter(view)
        return view
    }

    private fun initAdapter(view: View) {
        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        homeAdapter =
            HomeAdapter(requireContext(), homeResponse)
        recyclerView.adapter = homeAdapter
    }

}