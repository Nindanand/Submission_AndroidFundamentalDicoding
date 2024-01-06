package com.example.usergithub1.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserFavorite::class], version = 1, exportSchema = false)
abstract class DatabaseUser : RoomDatabase() {
    abstract fun UserFavoriteDao(): UserFavoriteDao

    companion object {
        @Volatile
        private var instance: DatabaseUser? = null
        fun getInstance(context: Context): DatabaseUser =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseUser::class.java, "database_user"
                ).build()
            }
    }
}