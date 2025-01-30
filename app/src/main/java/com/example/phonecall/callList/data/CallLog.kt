package com.example.phonecall.callList.data

import java.text.DateFormat
import java.util.Date

data class CallLogEntry(
    val id: Long,
    val phoneNumber: String,
    val type: CallType,
    val timestamp: Long,
    val name: String? = null
) {
    val formattedDateTime: String
        get() = DateFormat.getDateTimeInstance().format(Date(timestamp))
}

enum class CallType {
    INCOMING,
    OUTGOING,
    MISSED
}