package com.example.standardofsplit.View.Screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

@Composable
fun CalculatorScreen() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color(0xFFDCD0FF), shape = RoundedCornerShape(10.dp)) // 테두리 모양 적용
                    .clip(RoundedCornerShape(10.dp)) // Box 자체 모양을 동일하게 클리핑
                    .padding(16.dp) // 여백 추가 (선택 사항)
                    .height(50.dp)
                    .width(350.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "원",
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 40.sp
                )
            }
        }
    }
}