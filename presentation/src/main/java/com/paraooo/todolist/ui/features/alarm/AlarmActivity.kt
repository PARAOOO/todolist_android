package com.paraooo.todolist.ui.features.alarm

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi

class AlarmActivity : ComponentActivity() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isWakeLockReleased = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vibrate()

        val todoName = intent.getStringExtra("todoName") ?: "Todo 정보를 가져오지 못했습니다."

        // deprecated 플래그 사용 (잠금화면 위 + 화면 켜기)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 화면 깨우기 (FULL_WAKE_LOCK 사용)
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "todolist:alarmWakeLock"
        ).apply {
            acquire(60 * 1000L * 5) // 5분 유지
        }

        // Overlay 권한이 있으면 TYPE 설정 (원래 필요 없을 수도 있음)
        if (Settings.canDrawOverlays(this)) {
            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        }

        setContent {
            AlarmScreen(
                todoName = todoName,
                onDismiss = {
                    stopVibrate()
                    releaseWakeLock()
                    finish()
                }
            )
        }
    }

    override fun onPause() {
        super.onPause()
        releaseWakeLock()
    }

    override fun onStop() {
        super.onStop()
        stopVibrate()
        releaseWakeLock()
        finish()
    }

    private fun releaseWakeLock() {
        if (!isWakeLockReleased && wakeLock?.isHeld == true) {
            wakeLock?.release()
            isWakeLockReleased = true
        }
    }


    private fun vibrate() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val pattern = longArrayOf(0, 500, 500) // 대기 0ms → 진동 500ms → 정지 500ms → 반복

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createWaveform(pattern, 0) // 0부터 반복
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, 0)
        }
    }

    private fun stopVibrate() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        vibrator.cancel()
    }
}