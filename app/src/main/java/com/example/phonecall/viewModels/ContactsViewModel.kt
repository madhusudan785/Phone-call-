package com.example.phonecall.viewModels

import androidx.lifecycle.viewModelScope
import com.example.phonecall.callList.data.local.Contact
import kotlinx.coroutines.launch

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import com.example.phonecall.callList.data.local.AppPreferences
import com.example.phonecall.callList.data.local.ContactDatabase
import com.example.phonecall.callList.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val appPreferences: AppPreferences,
    application: Application
) : AndroidViewModel(application) {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private val _isFirstTime = MutableStateFlow(true)
    val isFirstTime = _isFirstTime.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allContacts.collect { _contacts.value = it }
        }
        checkFirstTime()
    }

    private fun checkFirstTime() {
        viewModelScope.launch {
            val firstTime = appPreferences.isFirstTime()
            _isFirstTime.value = firstTime
        }
    }

    fun fetchAndSaveContacts() {
        viewModelScope.launch {
            val contactsList = mutableListOf<Contact>()
            val contentResolver = getApplication<Application>().contentResolver
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ),
                null, null, null
            )

            cursor?.use {
                val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) {
                    val id = it.getLong(idIndex)
                    val name = it.getString(nameIndex)
                    val number = it.getString(numberIndex)
                    contactsList.add(Contact(id, name, number))
                }
            }
            repository.insertContacts(contactsList)


            appPreferences.setFirstTime(false)
            _isFirstTime.value = false
        }
    }

    fun addContact(name: String, phone: String) {
        viewModelScope.launch {
            val contact = Contact(
                id = 0,
                name = name,
                phoneNumber = phone
            )
            repository.insertContacts(listOf(contact))
        }
    }
}
