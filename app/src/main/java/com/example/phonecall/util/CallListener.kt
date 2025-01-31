package com.example.phonecall.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.example.phonecall.IncomingCallActivity


class CallListener(private val context: Context) : PhoneStateListener() {
    @Deprecated("Deprecated in Java")
    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        super.onCallStateChanged(state, phoneNumber)

        if (state == TelephonyManager.CALL_STATE_RINGING) {
            val contactName = getContactName(context, phoneNumber ?: "Unknown")

            // Open IncomingCallActivity with the caller's name
            val intent = Intent(context, IncomingCallActivity::class.java).apply {
                putExtra("CALLER_NAME", contactName)
                putExtra("CALLER_NUMBER", phoneNumber)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    // Function to fetch contact name
    private fun getContactName(context: Context, phoneNumber: String): String {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        var contactName: String? = null
        cursor?.use {
            if (it.moveToFirst()) {
                contactName = it.getString(it.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME))
            }
        }

        return contactName ?: "Unknown"
    }
}

