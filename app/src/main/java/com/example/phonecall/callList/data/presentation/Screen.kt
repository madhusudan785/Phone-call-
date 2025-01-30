package com.example.phonecall.callList.data.presentation

sealed class Screen(val route: String) {
    object Contacts : Screen("contacts")
    object CallLogs : Screen("call_logs")
}