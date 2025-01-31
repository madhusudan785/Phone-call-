package com.example.phonecall.callList.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val phoneNumber: String,
)