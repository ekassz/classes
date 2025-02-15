package com.cs407.lab7

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Please allow all notifications to continue.", Toast.LENGTH_LONG).show()
        }
    }

    @VisibleForTesting
    fun requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }
        if (ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSend: Button = findViewById(R.id.buttonSend)
        val editTextSender: EditText = findViewById(R.id.editTextSender)
        val editTextMessage: EditText = findViewById(R.id.editTextMessage)

        requestPermission()
        NotificationHelper.getInstance().createNotificationChannel(applicationContext)

        buttonSend.setOnClickListener {
            NotificationHelper.getInstance().setNotificationContent(
                editTextSender.text.toString(),
                editTextMessage.text.toString()
            )
            NotificationHelper.getInstance().showNotification(applicationContext)
        }

    }

}
