package com.example.standardofsplit.View.Activity

import StartScreen
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.standardofsplit.ViewModel.Start
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

class StartActivity : ComponentActivity() {

    private val start by viewModels<Start>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StandardOfSplitTheme {
                StartScreen(
                    start = start,
                    intentToReceiptActivity = {
                        val intent = Intent(this, ReceiptActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}