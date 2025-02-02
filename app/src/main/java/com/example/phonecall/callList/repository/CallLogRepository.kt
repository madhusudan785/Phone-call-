package com.example.phonecall.callList.repository

import com.example.phonecall.callList.data.local.CallLogDao
import com.example.phonecall.callList.model.CallLogEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CallLogRepository @Inject constructor(private val callLogDao: CallLogDao) {

    val allCallLogs: Flow<List<CallLogEntry>> = callLogDao.getAllCallLogs()

    suspend fun insertCallLogs(callLogs: List<CallLogEntry>) {
        callLogDao.insertCallLogs(callLogs)
    }
    fun getCallLogs(): Flow<List<CallLogEntry>> = callLogDao.getAllCallLogs()

}
