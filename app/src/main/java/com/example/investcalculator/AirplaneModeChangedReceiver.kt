package com.example.investcalculator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.AIRPLANE_MODE") {
            if (intent.getBooleanExtra("state", false)) {
                Toast.makeText(context, "Airplane mode is on", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Airplane mode is off", Toast.LENGTH_LONG).show()
            }
        }
    }
}
