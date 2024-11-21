package com.example.standardofsplit.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.standardofsplit.presentation.ui.screen.*
import com.example.standardofsplit.presentation.ui.theme.StandardOfSplitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        setContent {
            StandardOfSplitTheme {
                AppNavigation()
            }
        }
    }

    private fun setupWindow() {
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }
}

@Composable
private fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Start.route) {
        composable(Screen.Start.route) {
            StartScreen(
                onNext = { navController.navigate(Screen.Receipt.route) }
            )
        }
        composable(Screen.Receipt.route) {
            ReceiptScreen(
                onNext = { navController.navigate(Screen.Calculator.route) },
                onBack = { navController.navigate(Screen.Start.route) }
            )
        }
        composable(Screen.Calculator.route) {
            CalculatorScreen(
                onNext = { navController.navigate(Screen.Result.route) },
                onBack = { navController.navigate(Screen.Receipt.route) }
            )
        }
        composable(Screen.Result.route) {
            ResultScreen(
                onBack = { navController.navigate(Screen.Calculator.route) }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Start : Screen("StartScreen")
    object Receipt : Screen("ReceiptScreen")
    object Calculator : Screen("CalculatorScreen")
    object Result : Screen("ResultScreen")
}