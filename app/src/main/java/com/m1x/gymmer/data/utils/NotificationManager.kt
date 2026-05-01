package com.m1x.gymmer.data.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

class NotificationManager(private val context: Context) {

    fun sendWhatsAppMessage(phoneNumber: String, message: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
            intent.data = Uri.parse(url)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("NotificationManager", "Error sending WhatsApp: ${e.message}")
        }
    }

    fun sendSMS(phoneNumber: String, message: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:$phoneNumber")
            intent.putExtra("sms_body", message)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("NotificationManager", "Error sending SMS: ${e.message}")
        }
    }

    fun sendPaymentNotification(
        userName: String,
        amount: Double,
        isPartial: Boolean,
        userPhone: String?,
        trainerPhone: String?,
        ownerPhone: String?
    ) {
        val paymentType = if (isPartial) "Partial" else "Full"
        val message = "Payment Received!\nUser: $userName\nAmount: ₹$amount\nType: $paymentType Payment\nThank you for choosing Gymmer!"

        // Notify User
        userPhone?.let {
            sendWhatsAppMessage(it, message)
            sendSMS(it, message)
        }

        // Notify Trainer (if configured)
        trainerPhone?.let {
            sendWhatsAppMessage(it, "Gymmer Alert: Your trainee $userName has paid ₹$amount ($paymentType).")
        }

        // Notify Owner (if configured)
        ownerPhone?.let {
            sendWhatsAppMessage(it, "Gymmer Revenue Update: $userName paid ₹$amount via $paymentType payment.")
        }
    }
}
