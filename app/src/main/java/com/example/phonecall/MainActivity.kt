package com.example.phonecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.phonecall.callList.data.presentation.CallLogViewModel
import com.example.phonecall.callList.data.presentation.ContactViewModel
import com.example.phonecall.callList.data.presentation.PhoneAppNavigation
import com.example.phonecall.callList.data.presentation.screens.CallLogScreen
import com.example.phonecall.callList.data.presentation.screens.ContactScreen


class MainActivity : ComponentActivity() {
    private val contactViewModel: ContactViewModel by viewModels()
    private val callLogViewModel: CallLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhoneAppNavigation(navController = rememberNavController())
        }
    }
}



