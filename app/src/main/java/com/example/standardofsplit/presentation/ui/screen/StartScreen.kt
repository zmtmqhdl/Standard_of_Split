package com.example.standardofsplit.presentation.ui.screen

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.standardofsplit.R
import com.example.standardofsplit.domain.useCase.StartUseCase
import com.example.standardofsplit.presentation.event.StartScreenEvent
import com.example.standardofsplit.presentation.ui.component.SubmitButton
import com.example.standardofsplit.presentation.ui.component.CircleButton
import com.example.standardofsplit.presentation.ui.component.showCustomToast
import com.example.standardofsplit.presentation.ui.theme.Typography
import com.example.standardofsplit.presentation.viewModel.Start
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StartScreen(
    onNext: () -> Unit
) {
    val startViewModel: Start = hiltViewModel()  // 동일한 ViewModel 인스턴스 접근
    val personCount by startViewModel.personCount.collectAsState()

    val context = LocalContext.current

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
        onEvent = startViewModel::onEvent,
        personCount = personCount,
        onStart = onNext
    )
}

@Composable
private fun StartScreenContent(
    onEvent: (StartScreenEvent) -> Unit,
    onStart: () -> Unit,
    personCount: Int,
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
                onEvent = onEvent,
                count = personCount,
            )

            Text(text = "※ 인원 수를 선택해주세요 ※", style = Typography.#here..)
        }
        SubmitButton(text = "시작하기", onClick = onStart, modifier = Modifier.padding(top = 70.dp))
    }
}

@Composable
private fun PersonCountSelector(
    onEvent: (StartScreenEvent) -> Unit,
    count: Int,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CircleButton(
            text = "-",
            onClick = { onEvent(StartScreenEvent.OnDecrementClick) }
        )

        Text(text = "$count", modifier = Modifier.padding(horizontal = 40.dp), style = Typography.CountText)

        CircleButton(
            text = "+",
            onClick = { onEvent(StartScreenEvent.OnIncrementClick) }
        )
    }
}