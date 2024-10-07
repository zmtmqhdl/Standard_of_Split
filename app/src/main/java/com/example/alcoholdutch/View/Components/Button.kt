package com.example.alcoholdutch.View.Components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoholdutch.ui.theme.AlcoholDutchTheme

@Composable
fun BTN_Basic(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,  // 클릭 기능이 없는 버튼
        modifier = modifier
            .height(50.dp)
    ) {
        Text(
            text = content,
            fontSize = fontSize,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BTN_Circle(
    content: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier.size(50.dp),
    ) {
        Text(
            text = content,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

// 버튼이 글씨가 커지면 버튼에서 튕겨져 나감

@Preview
@Composable
fun ButtonPreview() {
    AlcoholDutchTheme {
        BTN_Circle(content = "+", onClick = {})
    }
}