package com.example.standardofsplit.View.Navigation

enum class NAV_Route(val route: String) {
    StartScreen("StartScreen"),
    MainScreen("MainScreen"),
    ReceiptScreen("ReceiptScreen")
}

//@Composable
//fun NAV(Controller: NavHostController, personnelCount: PersonnelCount) {
//    NavHost(navController = Controller, startDestination = NAV_Route.StartScreen.route) {
//        composable(NAV_Route.StartScreen.route) { StartScreen(personnelCount, Controller) }
//        composable(NAV_Route.ReceiptScreen.route) { ReceiptScreen() }
//        composable(NAV_Route.MainScreen.route) { CalculatorScreen() }
//    }
//}