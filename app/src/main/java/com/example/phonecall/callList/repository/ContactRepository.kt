package com.example.phonecall.callList.repository

import com.example.phonecall.callList.data.local.Contact
import com.example.phonecall.callList.data.local.ContactDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(private val contactDao: ContactDao) {
    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    suspend fun insertContacts(contacts: List<Contact>) {
        contactDao.insertAll(contacts)
    }

    suspend fun getContactName(phoneNumber: String?): String? {
        val contactName = contactDao.getContactName(phoneNumber ?: "")
        return contactName
    }
}
