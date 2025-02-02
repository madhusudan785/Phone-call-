package com.example.phonecall.viewModels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.provider.CallLog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.phonecall.callList.model.CallLogEntry
import com.example.phonecall.callList.repository.CallLogRepository
import com.example.phonecall.callList.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class CallLogViewModel @Inject constructor(
    private val repository: CallLogRepository,
    private val contactRepository: ContactRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _callLogs = MutableStateFlow<List<CallLogEntry>>(emptyList())
    val callLogs = _callLogs.asStateFlow()

    init {
        fetchCallLogs() // ✅ Automatically fetch on ViewModel creation
    }

    fun fetchCallLogs() {
        viewModelScope.launch(Dispatchers.IO) { // ✅ Run in Background Thread
            val callLogsList = mutableListOf<CallLogEntry>()
            val contentResolver = getApplication<Application>().contentResolver
            val cursor = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DATE,
                    CallLog.Calls.DURATION,
                    CallLog.Calls.TYPE
                ),
                null, null, "${CallLog.Calls.DATE} DESC"
            )

            cursor?.use {
                val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
                val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
                val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
                val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)

                var count = 0
                while (it.moveToNext() && count < 50) {
                    val phoneNumber = it.getString(numberIndex)
                    val callDate = DateFormat.getDateTimeInstance().format(Date(it.getLong(dateIndex)))

                    val totalSeconds = it.getInt(durationIndex)
                    val minutes = totalSeconds / 60
                    val seconds = totalSeconds % 60
                    val formattedDuration = if (minutes > 0) {
                        "${minutes}m ${seconds}s"
                    } else {
                        "${seconds}s"
                    }
                    val callType = when (it.getInt(typeIndex)) {
                        CallLog.Calls.INCOMING_TYPE -> "Incoming"
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                        CallLog.Calls.MISSED_TYPE -> "Missed"
                        else -> "Unknown"
                    }

                    val contactName = withContext(Dispatchers.IO) {
                        contactRepository.getContactName(phoneNumber) ?: phoneNumber
                    }

                    callLogsList.add(
                        CallLogEntry(
                            phoneNumber = phoneNumber,
                            callType = callType,
                            callDate = callDate,
                            callDuration = formattedDuration,
                            contactName = contactName
                        )
                    )
                    count++
                }
            }

            _callLogs.value = callLogsList
        }
    }


}

