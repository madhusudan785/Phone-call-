package com.example.phonecall.callList.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val phoneNumber: String,
)