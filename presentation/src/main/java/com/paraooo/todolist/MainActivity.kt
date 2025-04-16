package com.paraooo.todolist

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.paraooo.todolist.ui.navigation.AppNavGraph
import com.paraooo.todolist.ui.theme.TodoListTheme



class MainActivity : ComponentActivity() {

//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                // 권한 허용됨, 알림 가능
//                Toast.makeText(this, "알림 권한 허용됨", Toast.LENGTH_SHORT).show()
//            } else {
//                // 권한 거부됨
//                Toast.makeText(this, "알림 권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    private fun askNotificationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    android.Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // 권한 요청
//                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        askNotificationPermission()

        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            AppNavGraph(navController = navController)

        }
    }
}
