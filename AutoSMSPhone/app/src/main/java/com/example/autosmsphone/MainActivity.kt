package com.example.autosmsphone

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS = 1
    private lateinit var txtStatus: TextView
    private lateinit var btnToggle: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtStatus = findViewById(R.id.txtStatus)
        btnToggle = findViewById(R.id.btnToggle)
        sharedPreferences = getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE)

        checkPermissions()
        updateUI()

        btnToggle.setOnClickListener {
            toggleAutoReply()
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS
        )

        val permissionsNeeded = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), REQUEST_PERMISSIONS)
        } else {
            Toast.makeText(this, "Tất cả quyền đã được cấp!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Quyền đã được cấp!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ứng dụng cần quyền để hoạt động!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleAutoReply() {
        val isEnabled = sharedPreferences.getBoolean("autoReplyEnabled", false)
        val newState = !isEnabled

        sharedPreferences.edit().putBoolean("autoReplyEnabled", newState).apply()
        updateUI()

        val message = if (newState) "Chế độ tự động trả lời đã BẬT!" else "Chế độ tự động trả lời đã TẮT!"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUI() {
        val isEnabled = sharedPreferences.getBoolean("autoReplyEnabled", false)
        txtStatus.text = if (isEnabled) "Chế độ tự động trả lời: Bật" else "Chế độ tự động trả lời: Tắt"
        btnToggle.text = if (isEnabled) "Tắt chế độ tự động trả lời" else "Bật chế độ tự động trả lời"
    }
}
    