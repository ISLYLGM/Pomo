package com.example.pomodoro.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pomodoro.data.AppDatabase
import com.example.pomodoro.data.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val noteDao = database.noteDao()
    val coroutineScope = rememberCoroutineScope()

    var noteText by remember { mutableStateOf("") }

    // Carregar nota existente
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val notes = noteDao.getAllNotes().first()
            if (notes.isNotEmpty()) {
                noteText = notes.first().content
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título Perfil
        BasicText(
            text = "Perfil",
            style = TextStyle(
                color = Color(0xFF01579B),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Caixa de anotação
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            BasicTextField(
                value = noteText,
                onValueChange = { noteText = it },
                modifier = Modifier.fillMaxSize(),
                decorationBox = { innerTextField ->
                    if (noteText.isEmpty()) {
                        BasicText(
                            text = "Digite suas anotações aqui...",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        )
                    }
                    innerTextField()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Salvar
        Box(
            modifier = Modifier
                .background(Color(0xFF0288D1), shape = RoundedCornerShape(8.dp))
                .clickable {
                    coroutineScope.launch(Dispatchers.IO) {
                        val notes = noteDao.getAllNotes().first()
                        if (notes.isNotEmpty()) {
                            noteDao.update(notes.first().copy(content = noteText))
                        } else {
                            noteDao.insert(Note(content = noteText))
                        }
                    }
                }
                .padding(vertical = 12.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "Salvar",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
