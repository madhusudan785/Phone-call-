package com.example.phonecall.presentation.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

@Composable
fun IncomingCallScreen(callerName: String, callerNumber: String) {
    val context = LocalContext.current

    // Ask for SYSTEM_ALERT_WINDOW permission if not granted
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName))
        context.startActivity(intent)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = callerName, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = callerNumber, fontSize = 18.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = { acceptCall(context) },
                    colors = ButtonDefaults.buttonColors(Color.Green)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Answer", tint = Color.White)
                    Text("Answer")
                }
                Spacer(modifier = Modifier.width(32.dp))
                Button(
                    onClick = { rejectCall(context) },
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Decline", tint = Color.White)
                    Text("Decline")
                }
            }
        }
    }
}

fun acceptCall(context: Context) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        telecomManager.acceptRingingCall()
    } else {
        Toast.makeText(context, "Permission not granted to answer calls", Toast.LENGTH_SHORT).show()
    }
}

fun rejectCall(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
            telecomManager.endCall()
        } else {
            Toast.makeText(context, "Permission not granted to end calls", Toast.LENGTH_SHORT).show()
        }
    }
}
