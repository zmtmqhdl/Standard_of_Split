package com.example.standardofsplit.View.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.shadow


@Composable
fun BTN_Basic(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 26.sp,  // 기본 글자 크기
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(53.dp)
            .width(353.dp)
            .clip(RoundedCornerShape(28.dp))
            .shadow(4.dp, RoundedCornerShape(28.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1F4EF5),  // 배경색 변경
        )
    ) {
        Text(
            text = content,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun BTN_Circle(
    content: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    fontSize: TextUnit = 12.sp,
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
    StandardOfSplitTheme {
        BTN_Circle(content = "+", onClick = {})
    }
}