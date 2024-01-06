package com.example.usergithub1.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.usergithub1.entity.DatabaseUser
import com.example.usergithub1.entity.UserFavorite
import com.example.usergithub1.entity.UserFavoriteDao
import com.example.usergithub1.model.DetailUserResponse
import com.example.usergithub1.model.GithubResponse
import com.example.usergithub1.model.User
import com.example.usergithub1.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

import retrofit2.Response
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _listUsers = MutableLiveData<ArrayList<User>>()
    val listUsers: MutableLiveData<ArrayList<User>> = _listUsers

    private val _userdetail = MutableLiveData<DetailUserResponse>()
    val userdetail: MutableLiveData<DetailUserResponse> = _userdetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _userFollowers = MutableLiveData<List<User>?>()
    var userFollower: LiveData<List<User>?> = _userFollowers

    private val _userFollowing = MutableLiveData<List<User>?>()
    var userFollowing: LiveData<List<User>?> = _userFollowing


    private var userDao: UserFavoriteDao?
    private var dbUser: DatabaseUser?

    init {
        dbUser = DatabaseUser.getInstance(application)
        userDao = dbUser?.UserFavoriteDao()
    }

    fun getUserFavorite(): LiveData<List<UserFavorite>>? {
        return userDao?.getFavoriteUser()
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun findUserSearch(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                if (response.isSuccessful) {
                    val filteredUsers = response.body()?.items?.filter { user ->
                        user.login.contains(query, ignoreCase = true)
                    }
                    listUsers.postValue(filteredUsers as ArrayList<User>?)
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                Log.d(TAG, "OnFailure : ${t.message}" + t.message)
            }
        })
    }

    fun getAllUsers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers("a")
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                if (response.isSuccessful) {
                    listUsers.postValue(response.body()?.items)
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                Log.d(TAG, "OnFailure : ${t.message}" + t.message)
                _isLoading.value = false
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    _userFollowers.postValue(response.body())
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d(TAG, "OnFailure : ${t.message}")
                _isLoading.value = false
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    _userFollowing.postValue(response.body())
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d(TAG, "OnFailure : ${t.message}")
                _isLoading.value = false
            }
        })
    }


    fun getUserSearch(): MutableLiveData<ArrayList<User>> {
        return listUsers
    }

    fun setUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    userdetail.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.d(TAG, "OnFailure : ${t.message}" + t.message)
            }
        })
    }


    fun getUserDetail(): LiveData<DetailUserResponse> {
        return _userdetail
    }

    fun favoriteUser(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = UserFavorite(username, id, avatarUrl)
            userDao?.addFavorite(user)
        }
    }

    fun removeFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFavorite(id)
        }
    }

    suspend fun userCheck(id: Int) = userDao?.userCheck(id)
}