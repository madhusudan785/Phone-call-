package com.example.phonecall.util

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telecom.CallRedirectionService
import android.telecom.PhoneAccountHandle
import androidx.annotation.RequiresApi
import com.example.phonecall.IncomingCallActivity

@RequiresApi(Build.VERSION_CODES.Q)
abstract class MyCallRedirectionService : CallRedirectionService() {
    override fun onPlaceCall(p0: Uri, p1: PhoneAccountHandle, shouldCancel: Boolean) {

        val intent = Intent(this, IncomingCallActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)


        cancelCall()
    }
}
