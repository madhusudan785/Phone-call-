package com.example.phonecall.callList.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.phonecall.callList.model.CallLogEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface CallLogDao {

    @Query("SELECT * FROM call_logs ORDER BY callDate DESC")
    fun getAllCallLogs(): Flow<List<CallLogEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLogs(callLogs: List<CallLogEntry>)

    @Query("DELETE FROM call_logs")
    suspend fun clearCallLogs()

    @Query("SELECT * FROM call_logs WHERE phoneNumber = :phoneNumber ORDER BY callDate DESC")
    fun getCallLogsForContact(phoneNumber: String): Flow<List<CallLogEntry>>
}
