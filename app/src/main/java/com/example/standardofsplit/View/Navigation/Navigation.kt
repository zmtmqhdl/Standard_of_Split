package com.example.standardofsplit.View.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.standardofsplit.View.Activity.CalculatorScreen
import com.example.standardofsplit.View.Activity.ReceiptScreen
import com.example.standardofsplit.View.Activity.StartScreen

enum class NAV_Route(val route: String) {
    StartActivity("StartActivity"),
    MainActivity("MainActivity"),
    ReceiptActivity("ReceiptActivity")
}