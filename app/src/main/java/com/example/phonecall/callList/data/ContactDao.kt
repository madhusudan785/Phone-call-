package com.example.phonecall.callList.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<Contact>>

    @Insert
    suspend fun insertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contacts WHERE name LIKE :query OR phoneNumber LIKE :query")
    fun searchContacts(query: String): Flow<List<Contact>>

    @Update
    suspend fun updateContact(contact: Contact)

}