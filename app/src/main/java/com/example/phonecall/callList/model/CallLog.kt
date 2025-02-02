package com.example.phonecall.callList.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "call_logs")
data class CallLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val phoneNumber: String,
    val callType: String,
    val callDate: String,
    val callDuration: String,
    val contactName: String? = null
)