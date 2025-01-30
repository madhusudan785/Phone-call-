package com.example.phonecall.callList.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<Contact>)


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

    @Query("SELECT name FROM contacts WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getContactName(phoneNumber: String): String?


}