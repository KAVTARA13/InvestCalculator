package com.example.investcalculator.fragments

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.investcalculator.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var receiver: AirplaneModeChangedReceiver
    private var settingsData = ModelPreferencesManager.get<Settings>("KEY_Settings")

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { ModelPreferencesManager.with(it.application) }
        val clearWalletButton = view.findViewById<View>(R.id.clearWalletButton)
        clearWalletButton.setOnClickListener {
            App.instance.db.getStepDao().delete()
            Toast.makeText(context, "Wallet cleared", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<SwitchCompat>(R.id.darkModeSwitch).isChecked =
            settingsData?.darkMode != "0"

        view.findViewById<SwitchCompat>(R.id.compactModeSwitch).isChecked =
            settingsData?.compactMode != "0"

        view.findViewById<SwitchCompat>(R.id.airplaneMonitorSwitch).isChecked =
            settingsData?.bReceiver != "0"

        view.findViewById<EditText>(R.id.apiUR).setText(settingsData?.apiUR)
        view.findViewById<EditText>(R.id.dbUR).setText(settingsData?.dbUR)

        view.findViewById<EditText>(R.id.apiUR).addTextChangedListener {
            Settings.apiUR = it.toString()
            ModelPreferencesManager.put(Settings, "KEY_Settings")
        }
        view.findViewById<EditText>(R.id.dbUR).addTextChangedListener {
            Settings.dbUR = it.toString()
            ModelPreferencesManager.put(Settings, "KEY_Settings")
        }

        val darkModeSwitch = view.findViewById<SwitchCompat>(R.id.darkModeSwitch)
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Settings.darkMode = "1"
                Toast.makeText(context, "Dark mode enabled", Toast.LENGTH_SHORT).show()
            } else {
                Settings.darkMode = "0"
                Toast.makeText(context, "Dark mode disabled", Toast.LENGTH_SHORT).show()
            }
            ModelPreferencesManager.put(Settings, "KEY_Settings")
            Toast.makeText(context, "Restart app to apply changes", Toast.LENGTH_SHORT).show()
        }

        val airplaneMonitorSwitch = view.findViewById<SwitchCompat>(R.id.airplaneMonitorSwitch)
        airplaneMonitorSwitch.setOnCheckedChangeListener { _, isChecked ->
            receiver = AirplaneModeChangedReceiver()
            if (isChecked) {
                Settings.bReceiver = "1"
                Toast.makeText(context, "Airplane Monitoring Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Settings.bReceiver = "0"
                Toast.makeText(context, "Airplane Monitoring Disabled", Toast.LENGTH_SHORT).show()
            }
            ModelPreferencesManager.put(Settings, "KEY_Settings")
            Toast.makeText(context, "Restart app to apply changes", Toast.LENGTH_SHORT).show()
        }

        val compactModeSwitch = view.findViewById<SwitchCompat>(R.id.compactModeSwitch)
        compactModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Settings.compactMode = "1"
                Toast.makeText(context, "Compact mode enabled", Toast.LENGTH_SHORT).show()
            } else {
                Settings.compactMode = "0"
                Toast.makeText(context, "Compact mode disabled", Toast.LENGTH_SHORT).show()
            }
            ModelPreferencesManager.put(Settings, "KEY_Settings")
            Toast.makeText(context, "Restart app to apply changes", Toast.LENGTH_SHORT).show()
        }

        if (settingsData?.bReceiver == "1") {
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
                try {
                    context?.registerReceiver(receiver, it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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