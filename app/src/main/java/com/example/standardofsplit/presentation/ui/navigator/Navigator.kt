package com.example.standardofsplit.presentation.ui.navigator

import androidx.navigation.NavHostController
import com.example.standardofsplit.presentation.activity.Screen

class Navigator(private val navController: NavHostController) {
    fun navigateToStart() {
        navController.navigate(Screen.Start.route)
    }

    fun navigateToReceipt() {
        navController.navigate(Screen.Receipt.route)
    }

    fun navigateToCalculator() {
        navController.navigate(Screen.Calculator.route)
    }

    fun navigateToResult() {
        navController.navigate(Screen.Result.route)
    }
}