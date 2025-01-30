package com.example.phonecall.callList.data.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonecall.callList.data.CallLogEntry
import com.example.phonecall.callList.data.CallType
import com.example.phonecall.callList.data.presentation.CallLogsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallLogsScreen(
    viewModel: CallLogsViewModel = viewModel()
) {
    var showDialPad by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialPad = true }
            ) {
                Icon(Icons.Default.Person, "Open Dialpad")
            }
        }
    ) { padding ->
        CallLogsList(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )

        if (showDialPad) {
            ModalBottomSheet(
                onDismissRequest = { showDialPad = false }
            ) {
                DialPad(
                    number = phoneNumber,
                    onNumberChanged = { phoneNumber = it },
                    onCall = {
                        viewModel.initiateCall(phoneNumber)
                        showDialPad = false
                    }
                )
            }
        }
    }
}

@Composable
fun CallLogsList(
    modifier: Modifier = Modifier,
    viewModel: CallLogsViewModel = viewModel()
) {
    val callLogs by viewModel.callLogs.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = modifier
    ) {
        items(callLogs) { callLog ->
            CallLogItem(callLog = callLog)
        }
    }
}

@Composable
fun CallLogItem(
    callLog: CallLogEntry,
    onCallClick: (String) -> Unit = {}
) {
    ListItem(
        headlineContent =  { Text(callLog.name ?: callLog.phoneNumber) },
        supportingContent = {
            Row {
                Icon(
                    when (callLog.type) {
                        CallType.INCOMING -> Icons.Default.Call
                        CallType.OUTGOING -> Icons.Default.Call
                        CallType.MISSED -> Icons.Default.Call
                    },
                    contentDescription = null,
                    tint = when (callLog.type) {
                        CallType.MISSED -> MaterialTheme.colorScheme.error
                        else -> LocalContentColor.current
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text(callLog.formattedDateTime)
            }
        },
        leadingContent = {
            Icon(Icons.Default.Person, contentDescription = null)
        },
        trailingContent = {
            IconButton(onClick = { onCallClick(callLog.phoneNumber) }) {
                Icon(Icons.Default.Call, contentDescription = "Call")
            }
        }
    )
}


@Composable
fun DialPad(
    number: String,
    onNumberChanged: (String) -> Unit,
    onCall: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        // Number pad grid
        (1..9).chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { digit ->
                    DialpadButton(
                        text = digit.toString(),
                        onClick = { onNumberChanged(number + digit) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DialpadButton(text = "*", onClick = { onNumberChanged(number + "*") })
            DialpadButton(text = "0", onClick = { onNumberChanged(number + "0") })
            DialpadButton(text = "#", onClick = { onNumberChanged(number + "#") })
        }

        Button(
            onClick = onCall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Icon(Icons.Default.Call, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Call")
        }
    }
}
@Composable
fun DialpadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium
            )
            if (text.any { it.isDigit() }) {
                val letters = when (text) {
                    "2" -> "ABC"
                    "3" -> "DEF"
                    "4" -> "GHI"
                    "5" -> "JKL"
                    "6" -> "MNO"
                    "7" -> "PQRS"
                    "8" -> "TUV"
                    "9" -> "WXYZ"
                    else -> ""
                }
                if (letters.isNotEmpty()) {
                    Text(
                        text = letters,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
