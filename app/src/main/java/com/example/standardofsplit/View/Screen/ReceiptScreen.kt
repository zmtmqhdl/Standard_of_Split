package com.example.standardofsplit.View.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.standardofsplit.Model.ReceiptClass
import com.example.standardofsplit.View.Components.BTN_Basic
import com.example.standardofsplit.ViewModel.Receipt
import kotlinx.coroutines.launch

@Composable
fun ReceiptScreen(
    receipt: Receipt,
    intentToCalculatorActivity: () -> Unit
) {
    val receipts = receipt.receipts.observeAsState(initial = emptyList())

    ReceiptDetailList(receipts = receipts.value, receiptViewModel = receipt)

    Box(modifier = Modifier.fillMaxSize()) {
        BTN_Basic(
            content = "정산시작쓰",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = intentToCalculatorActivity
        )
    }
}


@Composable
fun ReceiptDetailList(
    receipts: List<ReceiptClass>,
    receiptViewModel: Receipt
) {
    val receiptlistState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            state = receiptlistState, // 스크롤 상태 지정
            modifier = Modifier
                .fillMaxWidth()
                .height(900.dp) // 동적으로 공간을 배분하여 버튼을 침범하지 않도록
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            items(receipts) { receipt ->
//                ReceiptDetailButton(
//                    receipt = receipt,
//                    onClick = { }
//                )
//            }
            itemsIndexed(receipts) { index, receipt ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(8.dp)
                ) {
                    Column (
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically // 수직 중앙 정렬
                        ) {
                            Text(
                                text = receipt.PlaceName,
                                modifier = Modifier
                                    .clickable(onClick = {
                                        println("텍스트 클릭됨!") // 로그 출력
                                    })
                                    .padding(start = 30.dp, end = 180.dp)
                            )
                            ElevatedButton(
                                onClick = { /* 버튼 클릭 시 동작 */ }
                            ) {
                                Text(text = "영수증 펼치기")
                            }
                        }
                        Text(
                            modifier = Modifier.padding(start = 30.dp),
                            text = "금액이얌"
                        )
                    }
                }
            }
            item {
                BTN_Basic(
                    content = "영수증 추가",
                    onClick = {
                        val newReceipt = ReceiptClass(
                            ReceiptNumber = receipts.size + 1,
                            PlaceName = "New Place",
                            ProductName = mutableListOf("Product 1"),
                            ProductQuantity = mutableListOf("1"),
                            ProductPrice = mutableListOf("100")
                        )
                        receiptViewModel.addReceipt(newReceipt)

                        coroutineScope.launch {
                            receiptlistState.animateScrollToItem(receipts.size)
                        }
                    }
                )
            }
        }
    }
}



@Composable
fun ReceiptDetailButton(
    receipt: ReceiptClass,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = "${receipt.ReceiptNumber}")
        Text(text = "${receipt.PlaceName}")
        Text(text = "${receipt.ProductName}")
        Text(text = "${receipt.ProductQuantity}")
        Text(text = "${receipt.ProductPrice}")
    }
}