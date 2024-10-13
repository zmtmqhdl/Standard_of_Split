package com.example.standardofsplit.View.Screen


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.View.Components.BTN_Basic
import com.example.standardofsplit.View.Components.BTN_Circle
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme
import com.example.standardofsplit.ViewModel.Receipt

@Composable
fun ReceiptScreen(
    receipt: Receipt
) {
    // 리스트 크기를 관리하는 상태 변수
    val count by receipt.count.observeAsState(1)
    val names: List<String> = List(count) { "$it" }

    Column {
        LazyColumn(modifier = Modifier
            .padding(vertical = 4.dp)
            .weight(1f)) {
            items(items = names) { name ->
                var expanded by rememberSaveable { mutableStateOf(false) }

                val extraPadding by animateDpAsState(
                    if (expanded) 48.dp else 0.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )

                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(24.dp)) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                        ) {
                            Text(text = "Hello, ")
                            Text(text = name)
                        }
                        ElevatedButton(
                            onClick = { expanded = !expanded }
                        ) {
                            Text(if (expanded) "Show less" else "Show more")
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // 버튼을 가로로 가운데 정렬
                ) {
                    BTN_Basic(
                        content = "영수증 추가",
                        modifier = Modifier
                            .padding(16.dp),
                        fontSize = 16.sp,
                        onClick = { receipt.increment() }
                    )
                }
            }
        }
        BTN_Basic(
            content = "정산",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 16.sp,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val dummyReceipt = Receipt()
    StandardOfSplitTheme {
        ReceiptScreen(
            receipt = dummyReceipt
        )
    }
}

