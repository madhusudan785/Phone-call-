package com.example.phonecall.callList.model


data class CallLogEntry(

    val phoneNumber: String,
    val callType: String,
    val callDate: String,
    val callDuration: String,
    val contactName: String? = null
)