package com.example.phonecall.callList.data.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import android.provider.CallLog
import com.example.phonecall.callList.data.CallLogEntry
import com.example.phonecall.callList.data.CallManager
import com.example.phonecall.callList.data.CallType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CallLogsViewModel(
    private val context: Context,
    private val callManager: CallManager
) : ViewModel() {
    private val _callLogs = MutableStateFlow<List<CallLogEntry>>(emptyList())
    val callLogs: StateFlow<List<CallLogEntry>> = _callLogs.asStateFlow()

    init {
        loadCallLogs()
    }

    private fun loadCallLogs() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val cursor = context.contentResolver.query(
                    CallLog.Calls.CONTENT_URI,
                    null,
                    null,
                    null,
                    "${CallLog.Calls.DATE} DESC"
                )

                cursor?.use { c ->
                    val logs = mutableListOf<CallLogEntry>()
                    val numberIndex = c.getColumnIndex(CallLog.Calls.NUMBER)
                    val typeIndex = c.getColumnIndex(CallLog.Calls.TYPE)
                    val dateIndex = c.getColumnIndex(CallLog.Calls.DATE)
                    val nameIndex = c.getColumnIndex(CallLog.Calls.CACHED_NAME)

                    while (c.moveToNext()) {
                        logs.add(CallLogEntry(
                            id = c.position.toLong(),
                            phoneNumber = c.getString(numberIndex),
                            type = when (c.getInt(typeIndex)) {
                                CallLog.Calls.INCOMING_TYPE -> CallType.INCOMING
                                CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
                                CallLog.Calls.MISSED_TYPE -> CallType.MISSED
                                else -> CallType.MISSED
                            },
                            timestamp = c.getLong(dateIndex),
                            name = c.getString(nameIndex)
                        ))
                    }
                    _callLogs.value = logs
                }
            }
        }
    }

    fun initiateCall(phoneNumber: String) {
        callManager.initiateCall(phoneNumber)
    }

    class Factory(
        private val context: Context,
        private val callManager: CallManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CallLogsViewModel::class.java)) {
                return CallLogsViewModel(context, callManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}