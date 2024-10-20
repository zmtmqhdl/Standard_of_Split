package com.example.standardofsplit.View.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.standardofsplit.View.Screen.ReceiptScreen
import com.example.standardofsplit.ViewModel.Receipt
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

class ReceiptActivity : ComponentActivity() {
        private val receipt by viewModels<Receipt>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                StandardOfSplitTheme {
                    ReceiptScreen(
                        receipt = receipt,
                        intentToCalculatorActivity = {
                            val intent = Intent(this, CalculatorActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

