package com.example.phonecall.callList.data.presentation.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonecall.callList.data.presentation.ContactViewModel
import com.example.phonecall.callList.data.presentation.components.AddContactDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(viewModel: ContactViewModel) {
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    val isFirstTime by viewModel.isFirstTime.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.fetchAndSaveContacts()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Contacts List") }) },
        floatingActionButton = {
            if (isFirstTime) {
                FloatingActionButton(
                    onClick = { requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS) }
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Load Contacts")
                }
            } else {
                Column {
                    FloatingActionButton(
                        onClick = { viewModel.fetchAndSaveContacts() },
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Contacts")
                    }
                    FloatingActionButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(Icons.Default.PersonAddAlt1, contentDescription = "Add Contact")
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (contacts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No contacts found. Load them or add new ones.")
                        }
                    }
                } else {
                    items(contacts) { contact ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = contact.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = contact.phoneNumber,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Show the dialog if `showDialog` is true
    if (showDialog) {
        AddContactDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, phone ->
                viewModel.addContact(name, phone)
                showDialog = false
            }
        )
    }
}




