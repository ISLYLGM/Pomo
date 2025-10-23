package com.example.pomodoro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    private val _secondsLeft = MutableStateFlow(25 * 60)
    val secondsLeft: StateFlow<Int> = _secondsLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    fun startTimer() {
        if (_isRunning.value) return
        _isRunning.value = true
        viewModelScope.launch {
            while (_isRunning.value && _secondsLeft.value > 0) {
                delay(1000L)
                _secondsLeft.value = _secondsLeft.value - 1
            }
            if (_secondsLeft.value == 0) _isRunning.value = false
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
    }

    fun resetTimer() {
        _isRunning.value = false
        _secondsLeft.value = 25 * 60
    }
}
