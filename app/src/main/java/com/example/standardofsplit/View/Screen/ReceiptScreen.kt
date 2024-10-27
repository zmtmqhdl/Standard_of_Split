package com.example.standardofsplit.View.Screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.standardofsplit.Model.ReceiptClass
import com.example.standardofsplit.View.Components.Basic_Button
import com.example.standardofsplit.View.Components.Circle_Button
import com.example.standardofsplit.View.Components.Elevated_Button
import com.example.standardofsplit.View.Components.Receipt_Add_Dialog
import com.example.standardofsplit.View.Components.Receipt_Name_Dialog
import com.example.standardofsplit.ViewModel.Receipt
import kotlinx.coroutines.launch

@Composable
fun ReceiptScreen(
    receipt: Receipt, intentToCalculatorActivity: () -> Unit
) {
    val receipts = receipt.receipts.observeAsState(initial = emptyList())

    ReceiptDetailList(receipts = receipts.value, receiptViewModel = receipt)

    Box(modifier = Modifier.fillMaxSize()) {
        Basic_Button(
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
    receipts: List<ReceiptClass>, receiptViewModel: Receipt
) {
    val receiptlistState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val expandedStates = remember { mutableStateListOf<Boolean>() }
    val addDialog = remember { mutableStateOf(false) }
    val nameDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(bottom = 16.dp),
    ) {
        LazyColumn(
            state = receiptlistState,
            modifier = Modifier
                .fillMaxWidth()
                .height(900.dp)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(receipts) { index, receipt ->
                expandedStates.add(false)
                val expanded = expandedStates[index]
                Card(
                    modifier = Modifier
                        .width(420.dp)
                        .wrapContentHeight()
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Column(
                        modifier = Modifier
                            .animateContentSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = receipt.PlaceName,
                                modifier = Modifier.clickable(onClick = { nameDialog.value = true})
                            )


                            Elevated_Button(content1 = "영수증 접기",
                                content2 = "영수증 펼치기",
                                flag = expanded,
                                onClick = { expandedStates[index] = !expanded }
                            )
                        }
                        if (expanded) {
                            Divider(modifier = Modifier.padding(top = 10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 8.dp),
                            ) {

                                Text(
                                    text = "상품명",
                                    modifier = Modifier.weight(1f),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "단가 (수량)",
                                    modifier = Modifier.weight(2f),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "금액",
                                    modifier = Modifier.weight(1f),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Divider()

                            val productNames = receipts[index].ProductName
                            val productPrices = receipts[index].ProductPrice
                            val productQuantities = receipts[index].ProductQuantity

                            for (i in productNames.indices) {
                                val productName = receipts[index].ProductName[i]
                                val price = receipts[index].ProductPrice[i]
                                val quantity = receipts[index].ProductQuantity[i]
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { println("안녕") },
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Text(
                                            text = productName,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "$price ($quantity)",
                                            modifier = Modifier.weight(2f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = 0.toString(),
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Circle_Button(
                                    content = "+",
                                    onClick = { addDialog.value = true }
                                )
                                if (addDialog.value) {
                                    Receipt_Add_Dialog(
                                        onDismiss = { addDialog.value = false },
                                        onConfirm = { /* 확인 버튼 로직 추가 */ },
                                        index = index
                                    )
                                }
                                if (nameDialog.value) {
                                    Receipt_Name_Dialog(
                                        onDismiss = { nameDialog.value = false },
                                        onConfirm = { /* 확인 버튼 로직 추가 */ },
                                        name = productNames[index]
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Basic_Button(content = "영수증 추가", onClick = {
                    val newReceipt = ReceiptClass(
                        ReceiptNumber = receipts.size,
                    )
                    receiptViewModel.addReceipt(newReceipt)

                    coroutineScope.launch {
                        receiptlistState.animateScrollToItem(receipts.size)
                    }
                })
            }
        }
    }
}


@Composable
fun ReceiptDetailButton(
    receipt: ReceiptClass, onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = "${receipt.ReceiptNumber}")
        Text(text = "${receipt.PlaceName}")
        Text(text = "${receipt.ProductName}")
        Text(text = "${receipt.ProductQuantity}")
        Text(text = "${receipt.ProductPrice}")
    }
}