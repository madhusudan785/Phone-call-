package com.example.phonecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.phonecall.presentation.screens.IncomingCallScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callerName = intent.getStringExtra("CALLER_NAME") ?: "Unknown"
        val callerNumber = intent.getStringExtra("CALLER_NUMBER") ?: "Unknown"
        setContent {
            IncomingCallScreen(callerName, callerNumber)
        }
    }
}
