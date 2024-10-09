package com.example.standardofsplit.View.Screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

@Composable
fun ReceiptScreen() {
    Text(
        text = "Hello",
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StandardOfSplitTheme {
        ReceiptScreen()
    }
}