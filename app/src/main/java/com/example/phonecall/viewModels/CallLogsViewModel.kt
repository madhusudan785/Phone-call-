package com.example.phonecall.viewModels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.provider.CallLog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonecall.callList.model.CallLogEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CallLogViewModel@Inject constructor(application: Application) : AndroidViewModel(application) {

    private val _callLogs = MutableStateFlow<List<CallLogEntry>>(emptyList())
    val callLogs = _callLogs.asStateFlow()

    init {
        fetchCallLogs() // Preload data
    }
    fun fetchCallLogs() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            if (context.checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

                return@launch
            }

            val callLogsList = mutableListOf<CallLogEntry>()

            val cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DATE,
                    CallLog.Calls.DURATION
                ),
                null, null, CallLog.Calls.DATE + " DESC"
            )

            cursor?.use {
                val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
                val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
                val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
                val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

                while (it.moveToNext()) {
                    val number = it.getString(numberIndex)
                    val type = when (it.getInt(typeIndex)) {
                        CallLog.Calls.INCOMING_TYPE -> "Incoming"
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                        CallLog.Calls.MISSED_TYPE -> "Missed"
                        else -> "Unknown"
                    }
                    val date = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                        .format(Date(it.getLong(dateIndex)))
                    val totalSeconds = it.getInt(durationIndex)
                    val minutes = totalSeconds / 60
                    val seconds = totalSeconds % 60
                    val formattedDuration = if (minutes > 0) {
                        "${minutes}m ${seconds}s"
                    } else {
                        "${seconds}s"
                    }

                    callLogsList.add(CallLogEntry(number, type, date, formattedDuration))
                }
            }

            _callLogs.value = callLogsList
        }
    }
}