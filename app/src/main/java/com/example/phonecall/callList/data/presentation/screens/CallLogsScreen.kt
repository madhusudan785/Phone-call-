package com.example.phonecall.callList.data.presentation.screens


import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonecall.R
import com.example.phonecall.callList.data.CallLogEntry
import com.example.phonecall.callList.data.presentation.CallLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallLogScreen(viewModel: CallLogViewModel) {
    val callLogs by viewModel.callLogs.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.fetchCallLogs()
        }
    }

    // Request the permission to read the call logs when the screen is launched
    LaunchedEffect(Unit) {
        requestPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG)
    }

    var isDialPadVisible by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Call Logs") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialPadVisible = true }
            ) {
                Icon(Icons.Default.Dialpad, contentDescription = "Dial Number")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isDialPadVisible) {
                // Show the dial pad if the button is clicked
                DialPad(
                    number = phoneNumber,
                    onNumberChanged = { phoneNumber = it },
                    onCall = { dialNumber(context, phoneNumber) }
                )
            } else {
                // Show the list of call logs if dial pad is not visible
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (callLogs.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No call logs found.")
                            }
                        }
                    } else {
                        items(callLogs) { log ->
                            CallLogItem(log)
                        }
                    }
                }
            }
        }
    }
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )


        }


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
            IconButton(
                onClick = {
                    if (number.isNotEmpty()) {
                        onNumberChanged(number.dropLast(1))
                    }
                },
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Backspace,
                    contentDescription = "Backspace",
                    modifier = Modifier.size(24.dp)
                )
            }
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
fun CallLogItem(callLog: CallLogEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = " ${callLog.phoneNumber}", style = MaterialTheme.typography.titleMedium)
            Icon(painter = if (callLog.callType=="Incoming") {
                                painterResource(R.drawable.phone_incoming)}
                            else if (callLog.callType=="Outgoing") {
                                painterResource(R.drawable.phone_outgoing)
            }
                    else{
                        painterResource(R.drawable.phone_missed)

            }, contentDescription = null)
            Text(text = "Date: ${callLog.callDate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Duration: ${callLog.callDuration}", style = MaterialTheme.typography.bodyMedium)
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

fun dialNumber(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}


//@Composable
//fun CallLogsList(
//    modifier: Modifier = Modifier,
//    viewModel: CallLogsViewModel = viewModel()
//) {
//    val callLogs by viewModel.callLogs.collectAsState(initial = emptyList())
//
//    LazyColumn(
//        modifier = modifier
//    ) {
//        items(callLogs) { callLog ->
//            CallLogItem(callLog = callLog)
//        }
//    }
//}
//
//@Composable
//fun CallLogItem(
//    callLog: CallLogEntry,
//    onCallClick: (String) -> Unit = {}
//) {
//    ListItem(
//        headlineContent =  { Text(callLog.name ?: callLog.phoneNumber) },
//        supportingContent = {
//            Row {
//                Icon(
//                    when (callLog.type) {
//                        CallType.INCOMING -> Icons.Default.Call
//                        CallType.OUTGOING -> Icons.Default.Call
//                        CallType.MISSED -> Icons.Default.Call
//                    },
//                    contentDescription = null,
//                    tint = when (callLog.type) {
//                        CallType.MISSED -> MaterialTheme.colorScheme.error
//                        else -> LocalContentColor.current
//                    }
//                )
//                Spacer(Modifier.width(8.dp))
//                Text(callLog.formattedDateTime)
//            }
//        },
//        leadingContent = {
//            Icon(Icons.Default.Person, contentDescription = null)
//        },
//        trailingContent = {
//            IconButton(onClick = { onCallClick(callLog.phoneNumber) }) {
//                Icon(Icons.Default.Call, contentDescription = "Call")
//            }
//        }
//    )
//}
//
//
