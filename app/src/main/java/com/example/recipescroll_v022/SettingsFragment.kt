package com.example.recipescroll_v022

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat


class SettingsFragment : PreferenceFragmentCompat() {



    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.d(TAG, "onCreate called")
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val userLogPreference = findPreference<ListPreference>("user_Log")
        userLogPreference?.setOnPreferenceClickListener {
            //findNavController().navigate(R.id.action_SettingsFragment_to_LogOutFragment)
            findNavController().navigate(R.id.action_SettingsFragment_to_LogOutFragment)
            true
        }

        val userSettingsPreference = findPreference<ListPreference>("user_Settings")
        userSettingsPreference?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_SettingsFragment_to_DeleteUserFragment)
            true
        }

        val switchPreferenceCompat = findPreference<SwitchPreferenceCompat>("dark_mode_switch")

        switchPreferenceCompat?.setOnPreferenceChangeListener { preference, newValue ->
            val isDarkModeOn = newValue as Boolean

            if (isDarkModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            requireActivity().recreate()

            true
        }

    }
}


