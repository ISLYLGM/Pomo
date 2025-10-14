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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import android.view.WindowInsetsController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.pomodoro.data.AppDatabase
import com.example.pomodoro.data.Note
import com.example.pomodoro.data.NoteDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura a janela (cor e transparência)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = 0xFF87CEFA.toInt() // azul pastel
        window.navigationBarColor = 0xFF87CEFA.toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView?.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        setContent { PomodoroApp() }
    }
}

/* ---------------- APP NAV ---------------- */
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
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .background(
                        color = Color(0xFFFFA07A),
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(top = 180.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("timer") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8EC6FF)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Estudar Agora")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("notes") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8EC6FF)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Bloco de Notas")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("profile") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8EC6FF)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Perfil")
            }
        }
    }
}


@Composable
fun TimerScreen(navController: NavHostController) {
    val totalTime = 25 * 60
    var secondsLeft by remember { mutableStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }

    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return "%02d:%02d".format(minutes, secs)
    }

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
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { isRunning = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) { Text("Iniciar", color = Color.White) }
                Button(
                    onClick = { isRunning = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) { Text("Pausar", color = Color.White) }
                Button(
                    onClick = {
                        isRunning = false
                        secondsLeft = totalTime
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) { Text("Reset", color = Color.White) }
            }
        }
    }
}


@Composable
fun NotesScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db: AppDatabase = remember { AppDatabase.getDatabase(context) }
    val noteDao: NoteDao = db.noteDao()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val notes by noteDao.getAllNotes().collectAsState(initial = emptyList<Note>())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(16.dp)
    ) {

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


        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título da nota") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Conteúdo da nota") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    scope.launch {
                        noteDao.insert(Note(title = title, content = content))
                        title = ""
                        content = ""
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8EC6FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Nota")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch { noteDao.delete(note) }
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(note.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(note.content, color = Color.DarkGray)
                    }
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
        Row(verticalAlignment = Alignment.CenterVertically) {
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

        val imageRes = if (selectedProfile == "Nina") R.drawable.girl else R.drawable.boy
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .border(
                    width = 4.dp,
                    color = Color(0xFF556B2F),
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

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
