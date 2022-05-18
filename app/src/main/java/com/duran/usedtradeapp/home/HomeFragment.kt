package com.duran.usedtradeapp.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.duran.usedtradeapp.R
import com.duran.usedtradeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var binding : FragmentHomeBinding? = null
    private lateinit var articleAdapter : ArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("sslee", "onViewCreated")

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        articleAdapter = ArticleAdapter()
        articleAdapter.submitList(mutableListOf<ArticleModel>().apply {
            add(ArticleModel("0", "AAA", 1000000, "5000원", ""))
            add(ArticleModel("0", "BBB", 2000000, "10000원", ""))
        })
    }

}