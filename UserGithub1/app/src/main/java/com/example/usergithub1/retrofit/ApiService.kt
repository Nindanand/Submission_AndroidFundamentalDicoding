package com.example.usergithub1.retrofit

import com.example.usergithub1.model.DetailUserResponse
import com.example.usergithub1.model.GithubResponse
import com.example.usergithub1.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") query: String,
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<User>>
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<User>>
}
