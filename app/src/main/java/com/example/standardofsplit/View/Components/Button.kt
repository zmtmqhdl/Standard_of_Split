package com.example.standardofsplit.View.Components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Basic_Button(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 26.sp,  // 기본 글자 크기
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(53.dp)
            .width(353.dp),
        shape = RoundedCornerShape(10.dp),  // 모서리를 살짝 둥글게 (4dp)
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1F4EF5),  // 배경색 변경
        )
    ) {
        Text(
            text = content,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 16.dp)

        )
    }
}

@Composable
fun Small_Button(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,  // 기본 글자 크기
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(33.dp)
            .width(73.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1F4EF5),
        )
    ) {
        Text(
            text = content,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,

        )
    }
}



@Composable
fun Elevated_Button(
    content1: String,
    content2: String,
    flag: Boolean,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1F4EF5)
        )
    ) {
        Text(text = if (flag) content1 else content2)
    }
}

@Composable
fun Circle_Button(
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
            fontSize = fontSize,
            textAlign = TextAlign.Center
        )
    }
}

// 버튼이 글씨가 커지면 버튼에서 튕겨져 나감

@Preview
@Composable
fun ButtonPreview() {
    StandardOfSplitTheme {
        Circle_Button(content = "+", onClick = {})
    }
}