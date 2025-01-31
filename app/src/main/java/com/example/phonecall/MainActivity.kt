package com.example.phonecall

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.phonecall.presentation.navigation.PhoneAppNavigation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            requestDefaultDialer(this)
            registerPhoneAccount(this)
            PhoneAppNavigation(navController = rememberNavController())
        }
    }
}


fun requestDefaultDialer(context: Context) {
    val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
    val packageName = context.packageName

    if (telecomManager.defaultDialerPackage != packageName) {
        val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
        intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
        context.startActivity(intent)
    }
}



fun registerPhoneAccount(context: Context) {
    val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
    val componentName = ComponentName(context, CallService::class.java)
    val phoneAccountHandle = PhoneAccountHandle(componentName, "MyDialer")

    val phoneAccount = PhoneAccount.builder(phoneAccountHandle, "My Dialer App")
        .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER)
        .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
        .build()

    telecomManager.registerPhoneAccount(phoneAccount)
}

