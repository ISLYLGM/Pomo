package com.example.pomodoro.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pomodoro.R
import com.example.pomodoro.data.Note
import com.example.pomodoro.viewmodel.NotesViewModel
import android.util.Log

@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = viewModel()
) {

    val notesList by viewModel.notes.collectAsState(initial = emptyList())
    var novaNota by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
            .padding(16.dp)
    ) {

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Voltar",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            BasicText(
                text = "Minhas Notas",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para nova nota
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = novaNota,
                onValueChange = { novaNota = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                textStyle = TextStyle(fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFF87CEFA), RoundedCornerShape(8.dp))
                    .padding(12.dp)
                    .clickable {
                        if (novaNota.text.isNotBlank()) {
                            try {
                                viewModel.addNote(novaNota.text)
                                novaNota = TextFieldValue("")
                            } catch (e: Exception) {
                                Log.e("NotesScreen", "Erro adicionando nota", e)
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "Adicionar",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de notas
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notesList) { note ->
                NoteItem(
                    note = note,
                    onDelete = { viewModel.deleteNote(note) },
                    onUpdate = { newContent ->
                        try {
                            viewModel.addNote(newContent)
                        } catch (e: Exception) {
                            Log.e("NotesScreen", "Erro atualizando nota", e)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, onDelete: () -> Unit, onUpdate: (String) -> Unit) {

    var editando by remember { mutableStateOf(false) }
    var textoEditado by remember { mutableStateOf(TextFieldValue(note.content)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF8E1), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (editando) {
            BasicTextField(
                value = textoEditado,
                onValueChange = { textoEditado = it },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(fontSize = 16.sp)
            )
        } else {
            BasicText(
                text = note.content,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Botão editar/salvar
        BasicText(
            text = if (editando) "✅" else "✏️",
            modifier = Modifier
                .clickable {
                    if (editando) {
                        if (textoEditado.text.isNotBlank()) {
                            onUpdate(textoEditado.text)
                            editando = false
                        }
                    } else {
                        textoEditado = TextFieldValue(note.content)
                        editando = true
                    }
                }
                .padding(horizontal = 8.dp),
            style = TextStyle(fontSize = 20.sp)
        )

        // Botão deletar
        BasicText(
            text = "🗑️",
            modifier = Modifier
                .clickable { onDelete() }
                .padding(horizontal = 8.dp),
            style = TextStyle(fontSize = 20.sp)
        )
    }
}
