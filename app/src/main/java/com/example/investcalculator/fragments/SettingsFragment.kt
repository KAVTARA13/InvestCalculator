package com.example.investcalculator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.investcalculator.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {


    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val darkModeSwitch = view.findViewById<Switch>(R.id.darkModeSwitch)
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Dark mode enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Dark mode disabled", Toast.LENGTH_SHORT).show()
            }
        }

    }
}