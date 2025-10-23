package com.example.pomodoro.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pomodoro.Nota
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.Image
import androidx.compose.ui.res.vectorResource

@Composable
fun NotesScreen(navController: NavController) {
    var notas = remember { mutableStateListOf(
        Nota(1, "Comprar leite"),
        Nota(2, "Estudar Kotlin"),
        Nota(3, "Fazer exercício")
    )}

    var novaNota by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
            .padding(16.dp)
    ) {
        // Input para nova nota
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
                            val id = if (notas.isEmpty()) 1 else notas.maxOf { it.id } + 1
                            notas.add(Nota(id, novaNota.text))
                            novaNota = TextFieldValue("")
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
            items(notas) { nota ->
                NotaItem(
                    nota = nota,
                    onDelete = { notas.remove(nota) },
                    onUpdate = { nova -> nota.texto = nova }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun NotaItem(nota: Nota, onDelete: () -> Unit, onUpdate: (String) -> Unit) {
    var editando by remember { mutableStateOf(false) }
    var textoEditado by remember { mutableStateOf(TextFieldValue(nota.texto)) }

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
                text = nota.texto,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Botão Editar / Salvar
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
                        editando = true
                    }
                }
                .padding(horizontal = 8.dp),
            style = TextStyle(fontSize = 20.sp)
        )

        // Botão Excluir
        BasicText(
            text = "🗑️",
            modifier = Modifier
                .clickable { onDelete() }
                .padding(horizontal = 8.dp),
            style = TextStyle(fontSize = 20.sp)
        )
    }
}

