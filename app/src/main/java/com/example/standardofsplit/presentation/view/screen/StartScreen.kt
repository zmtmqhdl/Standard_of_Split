package com.example.standardofsplit.presentation.view.screen

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.R
import com.example.standardofsplit.presentation.view.component.SubmitButton
import com.example.standardofsplit.presentation.view.component.CircleButton
import com.example.standardofsplit.presentation.view.component.showCustomToast
import com.example.standardofsplit.presentation.view.viewmodel.Start
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StartScreen(
    start: Start,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val personCount by start.personCount.observeAsState()
    var isToastShowing by remember { mutableStateOf(false) }
    var backPressedTime by remember { mutableStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime > 2000) {
            backPressedTime = currentTime
            showCustomToast(context, "한 번 더 뒤로가기 키를 누르면 종료됩니다.")
        } else {
            (context as? ComponentActivity)?.finish()
        }
    }

    fun showToastIfNotShowing() {
        if (!isToastShowing) {
            isToastShowing = true
            showCustomToast(context, "인원은 2 ~ 8명으로 설정 가능합니다.")
            MainScope().launch {
                delay(2000)
                isToastShowing = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = -70.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "영수증 아이콘",
            modifier = Modifier
                .size(380.dp)
                .offset(y = -20.dp),
            colorFilter = ColorFilter.tint(Color.DarkGray),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "N빵",
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow
            )
            Text(
                text = "의 정석",
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }

    StartScreenContent(
        personCount = personCount ?: 2,
        onIncrement = { 
            if (personCount == Start.MAX_PERSON_COUNT) {
                showToastIfNotShowing()
            } else {
                start.increment()
            }
        },
        onDecrement = { 
            if (personCount == Start.MIN_PERSON_COUNT) {
                showToastIfNotShowing()
            } else {
                start.decrement()
            }
        },
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
        Column(
            modifier = Modifier.offset(y = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PersonCountSelector(
                count = personCount,
                onIncrement = onIncrement,
                onDecrement = onDecrement
            )

            InstructionText()

            Spacer(modifier = Modifier.height(70.dp))
        }

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
        CircleButton(
            text = "-",
            onClick = onDecrement
        )

        CountText(count = count)

        CircleButton(
            text = "+",
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
        color = Color.White
    )
}

@Composable
private fun InstructionText() {
    Text(
        text = "※ 인원 수를 선택해주세요 ※",
        fontSize = 20.sp,
        color = Color.White
    )
}

@Composable
private fun StartButton(onClick: () -> Unit) {
    SubmitButton(
        text = "시작하기",
        onClick = onClick
    )
}