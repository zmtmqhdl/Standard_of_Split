package com.example.standardofsplit.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.standardofsplit.view.screen.CalculatorScreen
import com.example.standardofsplit.view.screen.ReceiptScreen
import com.example.standardofsplit.view.screen.StartScreen
import com.example.standardofsplit.view.screen.ResultScreen
import com.example.standardofsplit.viewmodel.Calculator
import com.example.standardofsplit.viewmodel.Receipt
import com.example.standardofsplit.viewmodel.Start
import com.example.standardofsplit.view.theme.StandardOfSplitTheme

class MainActivity : ComponentActivity() {
    private val viewModels by lazy {
        mapOf(
            "start" to viewModels<Start>().value,
            "receipt" to viewModels<Receipt>().value,
            "calculator" to viewModels<Calculator>().value
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StandardOfSplitTheme {
                AppNavigation(viewModels)
            }
        }
    }

    private fun setupWindow() {
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }
}

@Composable
private fun AppNavigation(viewModels: Map<String, Any>) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Start.route) {
        composable(Screen.Start.route) {
            StartScreen(
                start = viewModels["start"] as Start,
                onNext = { navController.navigate(Screen.Receipt.route) }
            )
        }
        composable(Screen.Receipt.route) {
            ReceiptScreen(
                receipt = viewModels["receipt"] as Receipt,
                onNext = { navController.navigate(Screen.Calculator.route) },
                onBack = { navController.navigate(Screen.Start.route) }
            )
        }
        composable(Screen.Calculator.route) {
            CalculatorScreen(
                calculator = viewModels["calculator"] as Calculator,
                receipt = viewModels["receipt"] as Receipt,
                start = viewModels["start"] as Start,
                onNext = { navController.navigate(Screen.Result.route) },
                onBack = { navController.navigate(Screen.Receipt.route) }
            )
        }
        composable(Screen.Result.route) {
            ResultScreen(
                start = viewModels["start"] as Start,
                calculator = viewModels["calculator"] as Calculator,
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