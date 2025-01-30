package com.example.phonecall.callList.data

import java.text.DateFormat
import java.util.Date


data class CallLogEntry(

    val phoneNumber: String,
    val callType: String,
    val callDate: String,
    val callDuration: String,
    val contactName: String? = null
)