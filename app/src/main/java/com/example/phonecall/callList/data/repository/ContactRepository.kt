package com.example.phonecall.callList.data.repository

import com.example.phonecall.callList.data.Contact
import com.example.phonecall.callList.data.ContactDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(private val contactDao: ContactDao) {
    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    suspend fun insertContacts(contacts: List<Contact>) {
        contactDao.insertAll(contacts)
    }
}
