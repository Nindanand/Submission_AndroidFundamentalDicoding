package com.example.usergithub1.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_favorite")
data class UserFavorite(
    @field:ColumnInfo(name = "username")
    val login: String,

    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String
)