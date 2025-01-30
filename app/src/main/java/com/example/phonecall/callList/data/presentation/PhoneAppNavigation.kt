package com.example.phonecall.callList.data.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.phonecall.callList.data.presentation.screens.CallLogsScreen
import com.example.phonecall.callList.data.presentation.screens.ContactsScreen


@Composable
fun PhoneAppNavigation(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == Screen.Contacts.route,
                    onClick = { navController.navigate(Screen.Contacts.route) },
                    icon = { Icon(Icons.Default.Person, "contacts") },
                    label = { Text("Contacts") }
                )

                NavigationBarItem(
                    selected = currentRoute == Screen.CallLogs.route,
                    onClick = { navController.navigate(Screen.CallLogs.route) },
                    icon = { Icon(Icons.Default.Call, "Call Logs") },
                    label = { Text("Call Logs") }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Contacts.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Contacts.route) { ContactsScreen() }
            composable(Screen.CallLogs.route) { CallLogsScreen() }
        }
    }
}