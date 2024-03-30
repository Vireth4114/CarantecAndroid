package com.example.carantecandroid

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IMemberDAO {
    @Insert fun insertOne(member: Member): Long

    @Query("SELECT * FROM member")
    fun getAll(): LiveData<List<Member>>

    @Query("DELETE FROM member")
    fun deleteAll()
}