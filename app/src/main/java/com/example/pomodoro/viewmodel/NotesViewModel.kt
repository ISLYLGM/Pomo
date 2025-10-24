package com.example.pomodoro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.AppDatabase
import com.example.pomodoro.data.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.util.Log

class NotesViewModel(app: Application) : AndroidViewModel(app) {
    private val noteDao = AppDatabase.getDatabase(app).noteDao()

    val notes: Flow<List<Note>> = noteDao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNote(content: String) {
        viewModelScope.launch {
            try {
                noteDao.insert(Note(content = content))
            } catch (e: Exception) {
                Log.e("NotesViewModel", "Erro adicionando nota", e)
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                noteDao.delete(note)
            } catch (e: Exception) {
                Log.e("NotesViewModel", "Erro deletando nota", e)
            }
        }
    }
}
