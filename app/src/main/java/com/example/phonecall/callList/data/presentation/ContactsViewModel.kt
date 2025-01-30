package com.example.phonecall.callList.data.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.phonecall.callList.data.Contact
import com.example.phonecall.callList.data.ContactDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val contactDao: ContactDao
) : ViewModel() {
    // Expose contacts as a Flow from Room
    val contacts: Flow<List<Contact>> = contactDao.getAllContacts()

    // Add a new contact
    fun addContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.insertContact(contact)
        }
    }

    // Delete a contact
    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.deleteContact(contact)
        }
    }

    // Search contacts by name or phone number
    fun searchContacts(query: String): Flow<List<Contact>> {
        return contactDao.searchContacts("%$query%")
    }

    // Update an existing contact
    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.updateContact(contact)
        }
    }

    // Factory for creating the ViewModel with dependencies
    class Factory(private val contactDao: ContactDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
                return ContactsViewModel(contactDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
