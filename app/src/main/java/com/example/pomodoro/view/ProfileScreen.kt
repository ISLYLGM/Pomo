package com.example.pomodoro.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pomodoro.R

@Composable
fun ProfileScreen(navController: NavHostController) {
    var selectedProfile by remember { mutableStateOf("Nina") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Voltar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Perfil do Usuário", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(32.dp))

        val imageRes = if (selectedProfile == "Nina") R.drawable.girl else R.drawable.boy

        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .border(width = 4.dp, color = Color(0xFF556B2F), shape = CircleShape)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Imagem de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Escolha seu avatar:", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Text(
                    "Nina",
                    fontSize = 22.sp,
                    color = if (selectedProfile == "Nina") Color(0xFF556B2F) else Color.Black,
                    modifier = Modifier.clickable { selectedProfile = "Nina" }
                )
                Text(
                    "Antonio",
                    fontSize = 22.sp,
                    color = if (selectedProfile == "Antonio") Color(0xFF556B2F) else Color.Black,
                    modifier = Modifier.clickable { selectedProfile = "Antonio" }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Nome: Usuário", fontSize = 20.sp)
            Text("Meta: Estudar 2h por dia", fontSize = 20.sp)
        }
    }
}
