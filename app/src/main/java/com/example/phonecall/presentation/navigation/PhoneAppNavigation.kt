package com.example.phonecall.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.phonecall.presentation.Screen
import com.example.phonecall.presentation.screens.CallLogScreen
import com.example.phonecall.presentation.screens.ContactScreen
import com.example.phonecall.viewModels.CallLogViewModel
import com.example.phonecall.viewModels.ContactViewModel

@Composable
fun PhoneAppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val callLogViewModel: CallLogViewModel = hiltViewModel()
    val contactViewModel: ContactViewModel = hiltViewModel()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == Screen.Contacts.route,
                    onClick = { navController.navigate(Screen.Contacts.route) },
                    icon = { Icon(Icons.Default.SupervisorAccount, contentDescription = "Contacts") },
                    label = { Text("Contacts") }
                )

                NavigationBarItem(
                    selected = currentRoute == Screen.CallLogs.route,
                    onClick = { navController.navigate(Screen.CallLogs.route) },
                    icon = { Icon(Icons.Default.Call, contentDescription = "Call Logs") },
                    label = { Text("Call Logs") }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.CallLogs.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Contacts.route) {
                ContactScreen(contactViewModel)
            }
            composable(Screen.CallLogs.route) {
                CallLogScreen(callLogViewModel)

            }
        }
    }
}
