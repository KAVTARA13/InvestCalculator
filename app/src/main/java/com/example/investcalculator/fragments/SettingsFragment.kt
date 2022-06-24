package com.example.investcalculator.fragments

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.investcalculator.AirplaneModeChangedReceiver
import com.example.investcalculator.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var receiver: AirplaneModeChangedReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val darkModeSwitch = view.findViewById<SwitchCompat>(R.id.darkModeSwitch)
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Dark mode enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Dark mode disabled", Toast.LENGTH_SHORT).show()
            }
        }

        val airplaneMonitorSwitch = view.findViewById<SwitchCompat>(R.id.airplaneMonitorSwitch)
        airplaneMonitorSwitch.setOnCheckedChangeListener { _, isChecked ->
            receiver = AirplaneModeChangedReceiver()
            if (isChecked) {
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
                    try {
                        context?.registerReceiver(receiver, it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                Toast.makeText(context, "Airplane Monitoring Enabled", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    context?.unregisterReceiver(receiver)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Toast.makeText(context, "Airplane Monitoring Disabled", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStop() {
        super.onStop()
        try {
            context?.unregisterReceiver(receiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}