package com.example.vdohvydoh

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        requestNotificationPermission()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BreathingApp()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "breathing_channel",
                "Дыхание",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о дыхании"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun BreathingApp() {
    var state by remember { mutableStateOf("idle") }
    var breathCount by remember { mutableIntStateOf(0) }
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val scale by animateFloatAsState(
        targetValue = when (state) {
            "inhale" -> 1f
            else -> 0.68f
        },
        animationSpec = tween(durationMillis = 1400, easing = CubicBezierEasing(0.45f, 0f, 0.55f, 1f)),
        label = "scale"
    )

    val statusText = when (state) {
        "idle" -> "Ты пока не дышишь"
        "inhale" -> "Вдох..."
        else -> "Выдох..."
    }

    val buttonText = if (state == "inhale") "Выдох" else "Вдох"

    fun showNotification() {
        val notification = NotificationCompat.Builder(context, "breathing_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Списание")
            .setContentText("СПИСАНО 129.95 РУБ")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        ) {
            NotificationManagerCompat.from(context).notify(breathCount, notification)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F2EC)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = statusText,
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF26302B),
                textAlign = TextAlign.Center,
                modifier = Modifier.height(80.dp),
                lineHeight = 48.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .size(320.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color(0xFFDADED4), CircleShape)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scale * 1.1f)
                        .background(Color(0x40A9BDB0), CircleShape)
                )

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                        if (state == "idle") {
                            state = "inhale"
                        } else if (state == "inhale") {
                            breathCount++
                            showNotification()
                            state = "exhale"
                        } else {
                            state = "inhale"
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scale),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF557565)
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp)
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFF1F2EC)
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Сделано вдохов: $breathCount",
                fontSize = 18.sp,
                color = Color(0xFF6B756F)
            )
        }
    }
}
