package com.example.alcoholdutch.View.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoholdutch.View.Activity.ui.theme.AlcoholDutchTheme
import com.example.alcoholdutch.View.Components.BTN_Basic
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .offset(y = 200.dp),  // 전체 Column을 위로 약간 이동
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BTN_Circle(content = "-")
            Text(
                text = "0",
                modifier = Modifier.padding(horizontal = 40.dp),
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )
            BTN_Circle(content = "+")
        }

        Spacer(modifier = Modifier.height(20.dp))  // Row와 BTN_Basic 사이에 간격 추가

        BTN_Basic(
            content = "정산하기",
            modifier = Modifier.fillMaxWidth(0.6f),
            fontSize = 25.sp,
        )
    }
}