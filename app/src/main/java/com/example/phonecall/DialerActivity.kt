package com.example.phonecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonecall.presentation.screens.DialerScreen
import com.example.phonecall.viewModels.DialerViewModel

class DialerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val phoneNumber = intent.data?.schemeSpecificPart ?: ""

        setContent {
            val viewModel: DialerViewModel = viewModel()
            LaunchedEffect(Unit) {
                viewModel.setPhoneNumber(phoneNumber)
            }
            DialerScreen(
                viewModel = viewModel,
                onClose = {finish()}
            )
        }
    }
}


