package com.example.phonecall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    DisposableEffect(permissionState) {
        onDispose {
            onPermissionsResult(permissionState.permissions.associate {
                it.permission to it.status.isGranted
            })
        }
    }
}