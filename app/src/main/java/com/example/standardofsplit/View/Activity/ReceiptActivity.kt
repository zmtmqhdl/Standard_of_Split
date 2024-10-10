package com.example.standardofsplit.View.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.standardofsplit.View.Screen.ReceiptScreen
import com.example.standardofsplit.ViewModel.ReceiptCount
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

class ReceiptActivity : ComponentActivity() {

    private val receiptCount by viewModels<ReceiptCount>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StandardOfSplitTheme {
                ReceiptScreen(
                    receiptCount = receiptCount
                )
            }
        }
    }
}