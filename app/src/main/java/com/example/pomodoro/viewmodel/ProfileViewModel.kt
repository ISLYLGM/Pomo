package com.example.pomodoro.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    val selectedProfile = mutableStateOf("Nina")

    fun selectProfile(name: String) {
        selectedProfile.value = name
    }
}
