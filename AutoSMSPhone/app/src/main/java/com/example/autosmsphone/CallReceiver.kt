package com.example.autosmsphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.telephony.TelephonyManager
import android.telephony.SmsManager
import android.widget.Toast

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                Toast.makeText(context, "Cuộc gọi đến từ: $incomingNumber", Toast.LENGTH_SHORT).show()
            } else if (state == TelephonyManager.EXTRA_STATE_IDLE && incomingNumber != null) {
                val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE)
                val isAutoReplyEnabled = sharedPreferences.getBoolean("autoReplyEnabled", false)

                if (isAutoReplyEnabled) {
                    sendAutoReply(context, incomingNumber)
                }
            }
        }
    }

    private fun sendAutoReply(context: Context?, phoneNumber: String) {
        try {
            val message = "Xin chào! Tôi hiện đang bận, sẽ gọi lại sau nhé."
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(context, "Đã gửi tin nhắn đến $phoneNumber", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Gửi tin nhắn thất bại!", Toast.LENGTH_SHORT).show()
        }
    }
}