package com.example.alcoholdutch.View.Components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoholdutch.ui.theme.AlcoholDutchTheme

@Composable
fun btn_basic(
    content: String,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White,
) {
    Button(
        onClick = {},  // 클릭 기능이 없는 버튼
        modifier = modifier,
    ) {
        Text(
            text = content,
            color = contentColor,
            fontSize = 12.sp
        )
    }
}

@Composable
fun BTN_Circle(
    content: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {},
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

@Preview
@Composable
fun ButtonPreview() {
    AlcoholDutchTheme {
        BTN_Circle(content = "+")
    }
}