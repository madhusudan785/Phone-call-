package com.example.phonecall

import androidx.compose.runtime.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(
    permissions: List<String>,
    onPermissionsResult: (Map<String, Boolean>) -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(permissions)

    // Request permissions on first composition
    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    // Observe permission changes and invoke the callback
    val permissionsResult = remember {
        derivedStateOf {
            permissionState.permissions.associate {
                it.permission to it.status.isGranted
            }
        }
    }

    LaunchedEffect(permissionsResult.value) {
        onPermissionsResult(permissionsResult.value)
    }
}
