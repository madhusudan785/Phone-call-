package com.example.phonecall.callList.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.phonecall.callList.model.CallLogEntry

@Database(entities = [CallLogEntry::class], version = 1)
abstract class CallLogDatabase : RoomDatabase() {
    abstract fun callLogDao(): CallLogDao
}
