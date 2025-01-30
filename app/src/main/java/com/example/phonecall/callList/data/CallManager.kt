package com.example.phonecall.callList.data

import android.content.Context
import android.content.Intent
import android.net.Uri

class CallManager(private val context: Context) {
    fun initiateCall(phoneNumber: String) {
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_CALL, uri)
        context.startActivity(intent)
    }
}