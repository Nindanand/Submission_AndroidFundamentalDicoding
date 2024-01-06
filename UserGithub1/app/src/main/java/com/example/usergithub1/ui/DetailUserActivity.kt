package com.example.usergithub1.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.usergithub1.R
import com.example.usergithub1.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.appName = resources.getString(R.string.app_name)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        if (username != null) {
            mainViewModel.setUserDetail(username)

            val followingFragment = FollowingFragment()
            followingFragment.arguments = Bundle().apply {
                putString(FollowersFragment.ARG_NAME, username)
                mainViewModel.getFollowing(username)
            }

            val followersFragment = FollowersFragment()
            followersFragment.arguments = Bundle().apply {
                putString(FollowingFragment.ARG_NAME, username)
                mainViewModel.getFollowers(username)
            }
        }


        mainViewModel.getUserDetail().observe(this){user ->
            showLoading(false)
            binding.apply {
                tvName.text = user.name
                tvUsername.text = user.login
                tvFollowers.text = "${user.followers} Followers"
                tvFollowing.text = "${user.following} Following"
                Glide.with(this@DetailUserActivity)
                    .load(user.avatarUrl)
                    .into(ivUser)
            }
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
