package com.example.standardofsplit.View.Activity

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
import com.example.standardofsplit.View.Screen.CalculatorScreen
import com.example.standardofsplit.View.Screen.ReceiptScreen
import com.example.standardofsplit.View.Screen.StartScreen
import com.example.standardofsplit.View.Screen.ResultScreen
import com.example.standardofsplit.ViewModel.Calculator
import com.example.standardofsplit.ViewModel.Receipt
import com.example.standardofsplit.ViewModel.Start
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

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
                onNext = { navController.navigate(Screen.Calculator.route) }
            )
        }
        composable(Screen.Calculator.route) {
            CalculatorScreen(
                calculator = viewModels["calculator"] as Calculator,
                receipt = viewModels["receipt"] as Receipt,
                start = viewModels["start"] as Start,
                onNext = { navController.navigate(Screen.Result.route) }
            )
        }
        composable(Screen.Result.route) {
            ResultScreen()
        }
    }
}

sealed class Screen(val route: String) {
    object Start : Screen("StartScreen")
    object Receipt : Screen("ReceiptScreen")
    object Calculator : Screen("CalculatorScreen")
    object Result : Screen("ResultScreen")
}