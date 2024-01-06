package com.example.usergithub1.model

import com.google.gson.annotations.SerializedName

data class GithubResponse(
    @field:SerializedName("items")
    val items: ArrayList<User>
)

data class User (

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("id")
    val id: Int,

)