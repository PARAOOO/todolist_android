package com.paraooo.todolist.ui.base

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {

    fun isPermissionGranted(context: Context, permission: String): Boolean {

        val isValidPermission = permission.startsWith("android.permission.")
        if (!isValidPermission) return false

        when(permission) {
            android.Manifest.permission.SCHEDULE_EXACT_ALARM -> {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager.canScheduleExactAlarms()
                } else {
                    true // Android 11 이하에서는 필요 없음
                }
            }
            android.Manifest.permission.SYSTEM_ALERT_WINDOW -> {
                return Settings.canDrawOverlays(context)
            }
            else -> {
                // 실제 권한 승인 여부 체크
                val result = ContextCompat.checkSelfPermission(context, permission)
                return result == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    fun shouldShowRationale(activity: ComponentActivity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    @Composable
    fun rememberPermissionRequestLauncher(
        permission: String,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ): () -> Unit {
        val context = LocalContext.current
        val activity = context as ComponentActivity

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                onGranted()
            } else {
                onDenied()
            }
        }

        return remember(permission) {
            {
//                when(permission) {
//                    android.Manifest.permission.SCHEDULE_EXACT_ALARM -> {
//                        if (isPermissionGranted(context, permission)) {
//                            onGranted()
//                        } else {
//                            openAppSettings(context, permission)
//                            onDenied() // 설정 다녀온 후 확인은 별도 처리 필요
//                        }
//                    }
//                    android.Manifest.permission.SYSTEM_ALERT_WINDOW -> {
//                        if (isPermissionGranted(context, permission)) {
//                            onGranted()
//                        } else {
//                            openAppSettings(context, permission)
//                            onDenied() // 설정 다녀온 후 확인은 별도 처리 필요
//                        }
//                    }
//                    else -> {
//                        when {
//                            isPermissionGranted(context, permission) -> onGranted()
//                            shouldShowRationale(activity, permission) -> {
//                                openAppSettings(context, permission)
//                                onDenied()
//                            }
//                            else -> {
//                                permissionLauncher.launch(permission)
//                            }
//                        }
//                    }
//                }

//                when {
//                    isPermissionGranted(context, permission) -> onGranted()
//                    shouldShowRationale(activity, permission) -> {
//                        openAppSettings(context, permission)
//                        onDenied()
//                    }
//                    else -> {
//                        permissionLauncher.launch(permission)
//                    }
//                }

                if (isPermissionGranted(context, permission)) {
                    onGranted()
                } else {
                    openAppSettings(context, permission)
                    onDenied() // 설정 다녀온 후 확인은 별도 처리 필요
                }

            }
        }
    }

    fun openAppSettings(context: Context, permission: String) {
        val intent = when (permission) {
            android.Manifest.permission.SCHEDULE_EXACT_ALARM -> {
                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
            }
            android.Manifest.permission.SYSTEM_ALERT_WINDOW -> {
//                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
//                    data = Uri.parse("package:${context.packageName}")
//                }
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
            }
            // 그 외 권한은 앱 설정 화면으로 이동
            else -> {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
            }
        }
        context.startActivity(intent)
    }

}