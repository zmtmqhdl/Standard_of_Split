package com.example.standardofsplit.View.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.View.Components.BTN_Basic
import com.example.standardofsplit.View.Components.BTN_Circle
import com.example.standardofsplit.ViewModel.PersonnelCount
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

class StartActivity : ComponentActivity() {

    private val personnelCount by viewModels<PersonnelCount>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StandardOfSplitTheme {
                StartScreen(personnelCount)  // ViewModel을 StartScreen에 전달
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartPreview() {
    // Preview에서는 ViewModel을 사용할 수 없으므로 더미 ViewModel을 전달합니다.
    val dummyPersonnelCount = PersonnelCount()  // 빈 ViewModel을 생성
    StandardOfSplitTheme {
        StartScreen(personnelCount = dummyPersonnelCount)
    }
}

@Composable
fun StartScreen(personnelCount: PersonnelCount) {

    val count by personnelCount.count.observeAsState(2)

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
            BTN_Circle(
                content = "-",
                onClick = { personnelCount.decrement()}  // 감소 버튼 클릭 시
            )
            Text(
                text = "$count",  // 숫자를 표시
                modifier = Modifier.padding(horizontal = 40.dp),
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )
            BTN_Circle(
                content = "+",
                onClick = { personnelCount.increment()}  // 증가 버튼 클릭 시
            )
        }

        Spacer(modifier = Modifier.height(20.dp))  // Row와 BTN_Basic 사이에 간격 추가

        BTN_Basic(
            content = "정산하기",
            modifier = Modifier.fillMaxWidth(0.6f),
            fontSize = 25.sp,
            onClick = {}
        )
    }
}