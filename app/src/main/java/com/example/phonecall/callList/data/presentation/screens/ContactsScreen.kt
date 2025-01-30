//package com.example.phonecall.callList.data.presentation.screens
//
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Call
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.ListItem
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.phonecall.callList.data.AppDatabase
//import com.example.phonecall.callList.data.Contact
//import com.example.phonecall.callList.data.presentation.ContactsViewModel
//
//@Composable
//fun ContactsScreen(
//    viewModel: ContactsViewModel = viewModel(
//        factory = ContactsViewModel.Factory(
//            AppDatabase.getDatabase(LocalContext.current).contactDao()
//        )
//    )
//) {
//    val contacts by viewModel.contacts.collectAsState(initial = emptyList())
//    var showAddContact by remember { mutableStateOf(false) }
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { showAddContact = true }
//            ) {
//                Icon(Icons.Default.Add, "Add Contact")
//            }
//        }
//    ) { padding ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//            items(contacts) { contact ->
//                ContactItem(contact = contact)
//            }
//        }
//    }
//
////    if (showAddContact) {
////        AddContactDialog(
////            onDismiss = { showAddContact = false },
////            onConfirm = {name,phone ->
////                viewModel.addContact(Contact(name = name, phoneNumber = phone))
////                showAddContact = false
////            }
////        )
////    }
//}
//
//
//
//@Composable
//fun ContactItem(contact: Contact) {
//    ListItem(
//        headlineContent = { Text(contact.name) },
//        supportingContent = { Text(contact.phoneNumber) },
//        leadingContent = {
//            Icon(Icons.Default.Person, contentDescription = null)
//        },
//        trailingContent = {
//            IconButton(onClick = { /* Initiate call */ }) {
//                Icon(Icons.Default.Call, contentDescription = "Call")
//            }
//        }
//    )
//}