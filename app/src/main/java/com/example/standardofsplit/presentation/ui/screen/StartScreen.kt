package com.example.standardofsplit.presentation.ui.screen

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.standardofsplit.R
import com.example.standardofsplit.presentation.event.StartEvent
import com.example.standardofsplit.presentation.preview.Pixel8ProPreview
import com.example.standardofsplit.presentation.ui.component.CircleButton
import com.example.standardofsplit.presentation.ui.component.SubmitButton
import com.example.standardofsplit.presentation.ui.component.showCustomToast
import com.example.standardofsplit.presentation.ui.theme.Typography
import com.example.standardofsplit.presentation.viewModel.StartViewModel
import kotlinx.coroutines.delay

@Composable
private fun PersonCountSelector(
    onEvent: (StartEvent) -> Unit,
    personCount: Int,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CircleButton(
            text = "-",
            onClick = { onEvent(StartEvent.OnDecrement) }
        )
        Text(
            text = "$personCount",
            modifier = Modifier.padding(horizontal = 40.dp),
            style = Typography.countTextStyle
        )
        CircleButton(
            text = "+",
            onClick = { onEvent(StartEvent.OnIncrement) }
        )
    }
}

@Composable
private fun StartScreenContent(
    onEvent: (StartEvent) -> Unit,
    onNext: () -> Unit,
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
            modifier = Modifier
                .offset(y = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PersonCountSelector(
                onEvent = onEvent,
                personCount = personCount,
            )
            Text(
                text = "※ 인원 수를 선택해주세요 ※",
                style = Typography.defaultTextStyle
            )
        }
        SubmitButton(
            text = "시작하기",
            onClick = onNext,
            modifier = Modifier
                .padding(top = 70.dp)
        )
    }
}

@Composable
fun StartScreen(
    onNext: () -> Unit,
) {
    val startViewModel: StartViewModel = hiltViewModel()

    val personCount by startViewModel.personCount.collectAsState()

    val context = LocalContext.current
    var isToastShowing by remember { mutableStateOf(false) }
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime > 2000) {
            backPressedTime = currentTime
            showCustomToast(context, "뒤로가기 키를 누르면 종료됩니다.")
        } else {
            (context as? AppCompatActivity)?.finish()
        }
    }

    if (personCount !in 2..8 && !isToastShowing) {
        isToastShowing = true
        showCustomToast(context, "인원은 2 ~ 8명으로 설정 가능합니다.")
        LaunchedEffect(Unit) {
            delay(2000)
            isToastShowing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-70).dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "영수증 아이콘",
            modifier = Modifier
                .size(380.dp)
                .offset(y = (-20).dp),
            colorFilter = ColorFilter.tint(Color.DarkGray),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "N빵",
                style = Typography.firstTitleTextStyle
            )
            Text(
                text = "의 정석",
                modifier = Modifier.padding(top = 24.dp),
                style = Typography.secondTitleTextStyle
            )
        }
    }
    StartScreenContent(
        onEvent = startViewModel::onEvent,
        onNext = onNext,
        personCount = personCount
    )
}

// Preview
@Preview
@Composable
fun Preview_PersonCountSelector() {
    PersonCountSelector(
        onEvent = { },
        personCount = 5
    )
}

@Preview
@Composable
fun Preview_StartScreenContent() {
    StartScreenContent(
        onEvent = { },
        onNext = { },
        personCount = 5
    )
}

@Preview(
    showBackground = true,
    widthDp = Pixel8ProPreview.WIDTH,
    heightDp = Pixel8ProPreview.HEIGHT
)
@Composable
fun Preview_StartScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        StartScreen(
            onNext = { },
        )
    }
}