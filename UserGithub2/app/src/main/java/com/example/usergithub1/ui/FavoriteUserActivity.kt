package com.example.usergithub1.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usergithub1.databinding.ActivityFavoriteUserBinding
import com.example.usergithub1.entity.UserFavorite
import com.example.usergithub1.model.User

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                Intent(this@FavoriteUserActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, user.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR, user.avatarUrl)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvFavoriteUser.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            rvFavoriteUser.setHasFixedSize(true)
            rvFavoriteUser.adapter = adapter
        }

    }

    private fun mapList(users: List<UserFavorite>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users) {
            val userMapped = User(
                user.login,
                user.avatarUrl,
                user.id,
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getUserFavorite()?.observe(this) { user ->
            if (user != null) {
                val list = mapList(user)
                adapter.setListUser(list)
            }
        }
    }
}