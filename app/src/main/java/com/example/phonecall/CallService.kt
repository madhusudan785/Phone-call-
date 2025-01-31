package com.example.phonecall


import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.util.Log

class CallService : ConnectionService() {

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.d("CallService", "Incoming call received")
        val connection = MyConnection()
        connection.setActive() // Set call as active
        return connection
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.d("CallService", "Outgoing call started")
        val connection = MyConnection()
        connection.setDialing() // Set call as dialing
        return connection
    }
}

