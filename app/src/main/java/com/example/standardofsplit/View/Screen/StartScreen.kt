package com.example.standardofsplit.View.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.View.Components.Basic_Button
import com.example.standardofsplit.View.Components.Circle_Button
import com.example.standardofsplit.View.Components.showCustomToast
import com.example.standardofsplit.ViewModel.Start
import com.example.standardofsplit.ui.theme.White

@Composable
fun StartScreen(
    start: Start,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val personCount by start.personCount.observeAsState()
    val showToast by start.showToast.observeAsState()

    LaunchedEffect(showToast) {
        if (showToast == true) {
            showCustomToast(context, "인원은 2명 이상 8명 이하로 설정 가능합니다.")
            start.resetToast()
        }
    }

    StartScreenContent(
        personCount = personCount ?: 2,
        onIncrement = { start.increment() },
        onDecrement = { start.decrement() },
        onStart = onNext
    )
}

@Composable
private fun StartScreenContent(
    personCount: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .offset(y = 220.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PersonCountSelector(
            count = personCount,
            onIncrement = onIncrement,
            onDecrement = onDecrement
        )

        Spacer(modifier = Modifier.height(10.dp))

        InstructionText()

        Spacer(modifier = Modifier.height(70.dp))

        StartButton(onClick = onStart)
    }
}

@Composable
private fun PersonCountSelector(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Circle_Button(
            content = "-",
            onClick = onDecrement
        )

        CountText(count = count)

        Circle_Button(
            content = "+",
            onClick = onIncrement
        )
    }
}

@Composable
private fun CountText(count: Int) {
    Text(
        text = "$count",
        modifier = Modifier.padding(horizontal = 40.dp),
        fontSize = 60.sp,
        fontWeight = FontWeight.Bold,
        color = White
    )
}

@Composable
private fun InstructionText() {
    Text(
        text = "※ 인원 수를 선택해주세요 ※",
        fontSize = 20.sp,
        color = White
    )
}

@Composable
private fun StartButton(onClick: () -> Unit) {
    Basic_Button(
        content = "시작하기",
        onClick = onClick
    )
}