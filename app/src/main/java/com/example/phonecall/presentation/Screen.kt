package com.example.phonecall.presentation

sealed class Screen(val route: String) {
    object Contacts : Screen("contacts")
    object CallLogs : Screen("call_logs")
}