package com.example.carantecandroid

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Member(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var licence: String,
    var name: String,
    var surname: String,
    var dives: Int,
    var date: String,
    var subdate: String,
    var pricing: String
)