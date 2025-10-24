package com.example.pomodoro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro.data.AppDatabase
import com.example.pomodoro.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.util.Log

class ProfileViewModel(private val database: AppDatabase) : ViewModel() {

    private val noteDao = database.noteDao()

    private val _noteText = MutableStateFlow("")
    val noteText: StateFlow<String> = _noteText

    fun loadNote() {
        viewModelScope.launch {
            try {
                val list = noteDao.getAllNotes().first()
                _noteText.value = list.firstOrNull()?.content ?: ""
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Erro carregando nota", e)
            }
        }
    }

    fun saveNote(content: String) {
        viewModelScope.launch {
            try {
                val list = noteDao.getAllNotes().first()
                val currentNote = list.firstOrNull()
                if (currentNote != null) {
                    noteDao.update(currentNote.copy(content = content))
                } else {
                    noteDao.insert(Note(content = content))
                }
                _noteText.value = content
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Erro salvando nota", e)
            }
        }
    }
}

class ProfileViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(database) as T
    }
}
