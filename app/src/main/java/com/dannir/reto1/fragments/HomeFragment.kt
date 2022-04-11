package com.dannir.reto1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dannir.reto1.databinding.FragmentHomeBinding
import com.dannir.reto1.model.PostAdapter
import com.dannir.reto1.model.mvvm.HomeViewModel
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private var postAdapter = PostAdapter()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.getPostList()

        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.homePostsRV.adapter = postAdapter
        binding.homePostsRV.layoutManager = LinearLayoutManager(context)
        binding.homePostsRV.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

        homeViewModel.postList.observe(viewLifecycleOwner) { list ->
            postAdapter.clear()

            list.reverse()

            list.forEach { post ->
                postAdapter.addPost(post)
            }
        }

        return binding.root
    }
}