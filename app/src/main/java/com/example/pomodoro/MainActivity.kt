package com.example.pomodoro

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import android.view.WindowInsetsController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = 0xFF87CEFA.toInt() // azul pastel
        window.navigationBarColor = 0xFF87CEFA.toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        setContent {
            PomodoroApp()
        }
    }
}

@Composable
fun PomodoroApp() {
    val navController = rememberNavController()

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("timer") { TimerScreen(navController) }
            composable("notes") { NotesScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.geminigenerated),
            contentDescription = "Imagem de fundo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔴 Card salmão atrás do texto
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .background(
                        color = Color(0xFFFFA07A), // salmão
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Bem-vindo Alunno!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Botões centralizados
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(top = 180.dp, bottom = 16.dp), // ajusta para ficar abaixo do texto
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("timer") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8EC6FF)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) { Text("Estudar Agora") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("notes") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8EC6FF)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) { Text("Bloco de Notas") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("profile") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8EC6FF)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) { Text("Perfil") }
        }
    }
}

@Composable
fun TimerScreen(navController: NavHostController) {
    val totalTime = 25 * 60 // 25 minutos
    var secondsLeft by remember { mutableStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }

    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return "%02d:%02d".format(minutes, secs)
    }

    // Contagem regressiva
    LaunchedEffect(isRunning) {
        while (isRunning && secondsLeft > 0) {
            kotlinx.coroutines.delay(1000L)
            secondsLeft--
        }
        if (secondsLeft == 0) isRunning = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // 🔝 Topo com seta + título
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Voltar"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pomodoro",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        // ⏱️ Timer centralizado
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7D32)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formatTime(secondsLeft),
                    fontSize = 48.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // 🔴 Botões vermelhos centralizados
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { isRunning = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Iniciar", color = Color.White)
                }

                Button(
                    onClick = { isRunning = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Pausar", color = Color.White)
                }

                Button(
                    onClick = {
                        isRunning = false
                        secondsLeft = totalTime
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Reset", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun NotesScreen(navController: NavHostController) {
    var text by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(16.dp)
    ) {
        // 🔙 Seta de voltar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Voltar"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Bloco de Notas", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Digite sua nota") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    notes = notes + text
                    text = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8EC6FF)),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Salvar") }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = note,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun ProfileScreen(navController: NavHostController) {
    var selectedProfile by remember { mutableStateOf("Nina") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔝 Topo com seta + título
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Voltar"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Perfil do Usuário",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Imagem do avatar
        val imageRes = if (selectedProfile == "Nina") R.drawable.girl else R.drawable.boy
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .border(
                    width = 4.dp,
                    color = Color(0xFF556B2F), // verde musgo
                    shape = CircleShape
                )
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

        // Conteúdo centralizado abaixo da imagem
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Escolha seu avatar:",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nina",
                    fontSize = 22.sp,
                    color = if (selectedProfile == "Nina") Color(0xFF556B2F) else Color.Black,
                    modifier = Modifier
                        .clickable { selectedProfile = "Nina" }
                        .padding(16.dp)
                )

                Text(
                    text = "Antonio",
                    fontSize = 22.sp,
                    color = if (selectedProfile == "Antonio") Color(0xFF556B2F) else Color.Black,
                    modifier = Modifier
                        .clickable { selectedProfile = "Antonio" }
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Nome: Usuario",
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "Meta: Estudar 2h por dia",
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 🔹 Quadro de Estudos após todas as informações
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quadro de Estudos",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.quadro_estudos), // substitua pela sua imagem
                contentDescription = "Quadro de Estudos",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentScale = ContentScale.FillWidth // mantém tamanho original se a imagem já estiver no tamanho certo
            )
        }
    }
}
