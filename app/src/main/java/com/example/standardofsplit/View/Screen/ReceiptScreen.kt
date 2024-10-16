package com.example.standardofsplit.View.Screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import com.example.standardofsplit.Model.items
import com.example.standardofsplit.View.Components.BTN_Basic
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme
import com.example.standardofsplit.ViewModel.Receipt

@Composable
fun ReceiptScreen(
    receipt: Receipt,
    intentToCalculatorActivity: () -> Unit
) {
    val items by receipt.items.observeAsState(listOf())

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .weight(1f)
        ) {
            items(items) { item ->
                ReceiptItem(item)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    BTN_Basic(
                        content = "영수증 추가",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        onClick = { receipt.addItem() }
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

@Composable
fun ReceiptItem(item: items) {
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
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                ) {
                    Text(text = item.ReceiptName)
                    val totalPrice = item.MenuQuantity.zip(item.MenuPrice) { quantity, price ->
                        quantity.replace("개", "").toInt() * price.replace("원", "").toInt()
                    }.sum()
                    Text(text = "${totalPrice}원")
                }
                if (expanded) {
                    BTN_Basic(
                        content = "내역 추가",
                        modifier = Modifier.fillMaxWidth(0.3f),
                        fontSize = 16.sp,
                        onClick = {
                        }
                    )
                }
                ElevatedButton(
                    onClick = { expanded = !expanded }
                ) {
                    Text(if (expanded) "영수증 접기" else "영수증 펼치기")
                }
            }
            if (expanded) {
                Column {
                    // 헤더 추가
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "상품명", modifier = Modifier.weight(1f)) // 상품명 헤더
                        Text(text = "개당 가격", modifier = Modifier.padding(start = 16.dp)) // 개당 가격 헤더
                        Text(text = "수량", modifier = Modifier.padding(start = 16.dp)) // 수량 헤더
                        Text(text = "총 가격", modifier = Modifier.padding(start = 16.dp)) // 총 가격 헤더
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    // 각 메뉴 항목 출력
                    item.MenuName.forEachIndexed { index, menu ->
                        // 수량과 가격을 정수로 변환
                        val quantity = item.MenuQuantity[index].replace("개", "").toInt()
                        val price = item.MenuPrice[index].replace("원", "").toInt()

                        // 총 가격 계산
                        val totalItemPrice = quantity * price

                        // Row 구성
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = menu, modifier = Modifier.weight(1f)) // 상품명
                            Text(text = "${price}원", modifier = Modifier.padding(start = 16.dp)) // 개당 가격
                            Text(text = "${item.MenuQuantity[index]}", modifier = Modifier.padding(start = 16.dp)) // 수량
                            Text(text = "${totalItemPrice}원", modifier = Modifier.padding(start = 16.dp)) // 총 가격
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun MenuDialog (
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // 값 수정 로직 추가
//                onConfirm(updatedMenuName, updatedQuantity, updatedPrice)
                onDismiss
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        },
        title = { Text("항목 수정") },
        text = {
            Column {
                // 각 값을 수정할 수 있는 TextField 등 입력 UI 추가
                TextField(value = "a", onValueChange = { /* 수정된 메뉴명 처리 */ })
                TextField(value = "quantity".toString(), onValueChange = { /* 수정된 수량 처리 */ })
                TextField(value = "price".toString(), onValueChange = { /* 수정된 가격 처리 */ })
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ReceiptPreview() {
    val dummyReceipt = Receipt()
    StandardOfSplitTheme {
        ReceiptScreen(
            receipt = dummyReceipt,
            intentToCalculatorActivity = {}
        )
    }
}
