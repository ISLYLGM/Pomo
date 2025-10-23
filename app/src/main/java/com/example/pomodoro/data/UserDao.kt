package com.example.pomodoro.data

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE username = :name")
    suspend fun getUser(name: String): User?

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)
}
