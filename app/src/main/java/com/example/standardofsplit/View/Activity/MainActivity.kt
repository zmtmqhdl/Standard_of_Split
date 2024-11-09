package com.example.standardofsplit.View.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.standardofsplit.View.Screen.CalculatorScreen
import com.example.standardofsplit.View.Screen.ReceiptScreen
import com.example.standardofsplit.View.Screen.StartScreen
import com.example.standardofsplit.ViewModel.Calculator
import com.example.standardofsplit.ViewModel.Receipt
import com.example.standardofsplit.ViewModel.Start
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

class MainActivity : ComponentActivity() {

    private val start by viewModels<Start>()
    private val receipt by viewModels<Receipt>()
    private val calculator by viewModels<Calculator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            StandardOfSplitTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "StartScreen"
                ) {
                    composable("StartScreen") {
                        StartScreen(
                            start = start,
                            onNext = { navController.navigate("ReceiptScreen") }
                        )
                    }
                    composable("ReceiptScreen") {
                        ReceiptScreen(
                            receipt = receipt,
                            onNext = { navController.navigate("CalculatorScreen") }
                        )
                    }
                    composable("CalculatorScreen") {
                        CalculatorScreen(
                            calculator = calculator,
                            receipt = receipt,
                            start = start
                        )
                    }
                }
            }
        }
    }
}