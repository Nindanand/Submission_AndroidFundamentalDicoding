package com.example.usergithub1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usergithub1.databinding.FollowFragmentBinding

class FollowersFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FollowFragmentBinding
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FollowFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        adapter = UserAdapter()

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.adapter = adapter

        val username = ""
        mainViewModel.getFollowers(username)

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        mainViewModel.userFollower.observe(viewLifecycleOwner) { followers ->
            followers?.let {
                adapter.setListUser(it)
            }
        }
    }

    companion object {
        const val ARG_NAME = "username"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}