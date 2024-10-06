package com.example.alcoholdutch.View.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.alcoholdutch.View.Activity.ui.theme.AlcoholDutchTheme
import com.example.alcoholdutch.View.Components.BTN_Circle

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcoholDutchTheme {
                StartScreen()
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun StartActivityPreview() {
    AlcoholDutchTheme {
        StartScreen()
    }
}

@Preview(showBackground = true, widthDp = 200, heightDp = 200)
@Composable
fun StartScreen() {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        BTN_Circle(content = "+")
        Text(
            text = "0",
            textAlign = TextAlign.Center
        )
        BTN_Circle(content = "-")
    }
}