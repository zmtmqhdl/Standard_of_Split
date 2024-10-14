package com.example.standardofsplit.View.Screen


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.View.Components.BTN_Basic
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme
import com.example.standardofsplit.ViewModel.Receipt

@Composable
fun ReceiptScreen(
    receipt: Receipt,
    intentToCalculatorActivity: () -> Unit
) {
    val menuName by receipt.menuName.observeAsState("")
    val menuQuantity by receipt.menuQuantity.observeAsState("1")
    val menuPrice by receipt.menuPrice.observeAsState("")
    val items by receipt.items.observeAsState(listOf())

    Column {
        LazyColumn(modifier = Modifier
            .padding(vertical = 4.dp)
            .weight(1f)) {
            items(items = items) { (name, price) ->
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
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                            ) {
                                Text(text = name) // 메뉴 이름
                                if (expanded) {
                                    Text(text = price) // 가격
                                }
                            }
                            ElevatedButton(
                                onClick = { expanded = !expanded }
                            ) {
                                Text(if (expanded) "Show less" else "Show more")
                            }
                        }
                        if (expanded) {
                            // 확장되면 메뉴 이름 | 가격 형식으로 출력
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "메뉴 이름")
                                Text(text = "가격")
                            }
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = name)  // 실제 메뉴 이름
                                Text(text = price) // 실제 가격
                            }
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
            onClick = { intentToCalculatorActivity() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val dummyReceipt = Receipt()
    StandardOfSplitTheme {
        ReceiptScreen(
            receipt = dummyReceipt,
            intentToCalculatorActivity = {}
        )
    }
}

