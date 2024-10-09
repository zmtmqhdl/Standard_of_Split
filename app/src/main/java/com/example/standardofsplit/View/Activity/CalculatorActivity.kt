package com.example.standardofsplit.View.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

class CalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StandardOfSplitTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    Text(
        text = "Hello"
    )
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    StandardOfSplitTheme {
        CalculatorScreen()
    }
}