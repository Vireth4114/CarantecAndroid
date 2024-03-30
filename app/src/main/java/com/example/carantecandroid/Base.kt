package com.example.carantecandroid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [Member::class],
        version = 1
)
abstract class Base: RoomDatabase() {
    abstract fun memberDAO(): IMemberDAO

    companion object {
        private var instance: Base? = null
        fun getInstance(context: Context): Base {
            if (instance == null)
                instance = Room.databaseBuilder(
                    context,
                    Base::class.java, "modules.sqlite"
                ).build()
            return instance!!
        }
    }
}