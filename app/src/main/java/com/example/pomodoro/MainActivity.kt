package com.example.pomodoro

import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pomodoro.ui.theme.PomodoroTheme
import com.example.pomodoro.view.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Personaliza a cor da status bar e barra de navegação
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = 0xFF87CEFA.toInt() // azul pastel
        window.navigationBarColor = 0xFF87CEFA.toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView?.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        setContent {
            PomodoroTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") { HomeScreen(navController) }
                    composable("timer") { TimerScreen(navController) }
                    composable("notes") { NotesScreen(navController) }
                    composable("profile") { ProfileScreen(navController) }
                }
            }
        }
    }
}
