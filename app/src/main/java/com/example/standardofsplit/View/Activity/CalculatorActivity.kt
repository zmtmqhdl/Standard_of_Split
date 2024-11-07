package com.example.standardofsplit.View.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.example.standardofsplit.View.Screen.CalculatorScreen
import com.example.standardofsplit.ViewModel.Calculator
import com.example.standardofsplit.ViewModel.Start
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

class CalculatorActivity : ComponentActivity() {

    private val calculator by viewModels<Calculator>()
    private val start by viewModels<Start>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            StandardOfSplitTheme {
                CalculatorScreen(
                    calculator = calculator,
                    start = start
                )
            }
        }
    }
}