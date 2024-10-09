package com.example.standardofsplit.View.Screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

@Composable
fun CalculatorScreen() {
    Text(
        text = "Helldddddo"
    )
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    StandardOfSplitTheme {
        CalculatorScreen()
    }
}