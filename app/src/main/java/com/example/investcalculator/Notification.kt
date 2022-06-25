package com.example.investcalculator

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

object Notification {
    const val CHANNEL_ID = "messages";
    fun showNotification(context: Context){
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("title")
            .setContentText("shetyobineba")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1,notificationBuilder.build())
    }
}