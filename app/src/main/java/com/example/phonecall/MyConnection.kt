package com.example.phonecall

import android.telecom.Connection
import android.telecom.DisconnectCause
import android.util.Log

class MyConnection : Connection() {
    override fun onAnswer() {
        Log.d("MyConnection", "Call answered")
        setActive() // Call is now active
    }

    override fun onDisconnect() {
        Log.d("MyConnection", "Call disconnected")
        setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
        destroy()
    }

    override fun onHold() {
        Log.d("MyConnection", "Call on hold")
        setOnHold()
    }

    override fun onUnhold() {
        Log.d("MyConnection", "Call resumed")
        setActive()
    }
}

