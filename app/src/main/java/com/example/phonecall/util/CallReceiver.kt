package com.example.phonecall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val incomingCallIntent = Intent(context, IncomingCallActivity::class.java)
                incomingCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(incomingCallIntent)
            }
        }
    }
}

