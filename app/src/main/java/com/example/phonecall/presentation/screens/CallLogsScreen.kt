package com.example.phonecall.presentation.screens


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonecall.R
import com.example.phonecall.callList.model.CallLogEntry
import com.example.phonecall.viewModels.CallLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallLogScreen(viewModel: CallLogViewModel) {
    val callLogs by viewModel.callLogs.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isDialPadVisible by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        if (context.checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_CALL_LOG),
                101
            )
        } else {
            viewModel.fetchCallLogs()
        }
    }
    if (callLogs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {

        Scaffold(
            topBar = { TopAppBar(title = { Text("Call Logs") }) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { isDialPadVisible = true } // ✅ Show Dialer Bottom Sheet
                ) {
                    Icon(
                        imageVector = Icons.Default.Dialpad,
                        contentDescription = "Open Dialer"
                    )
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                LazyColumn {
                    items(callLogs) { log ->
                        CallLogItem(
                            log
                        )
                    }
                }
            }

            if (isDialPadVisible) {
                ModalBottomSheet(
                    onDismissRequest = { isDialPadVisible = false },
                    sheetState = sheetState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight() // ✅ Ensures it stays at the bottom
                ) {
                    DialerScreen(onClose = { isDialPadVisible = false })
                }
            }
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
            Text(
                text = callLog.contactName ?: callLog.phoneNumber,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Date: ${callLog.callDate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Duration: ${callLog.callDuration}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}




