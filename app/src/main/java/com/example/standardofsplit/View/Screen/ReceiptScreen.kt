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
import com.example.standardofsplit.View.Components.Receipt_Change_Dialog
import com.example.standardofsplit.View.Components.Receipt_Name_Dialog
import com.example.standardofsplit.ViewModel.Receipt
import kotlinx.coroutines.launch

@Composable
fun ReceiptScreen(
    receipt: Receipt,
    intentToCalculatorActivity: () -> Unit
) {
    val receipts = receipt.receipts.observeAsState(initial = emptyList())
    val addDialog = remember { mutableStateOf(false) }
    val nameDialog = remember { mutableStateOf(false) }
    val changeDialog = remember { mutableStateOf(false) }
    val selectedReceiptIndex = remember { mutableStateOf(-1) }
    val selectedReceiptIIndex = remember { mutableStateOf(-1) }

    ReceiptDetailList(
        receipts = receipts.value,
        receiptViewModel = receipt,
        onAddClick = { index, iindex ->
            selectedReceiptIndex.value = index;
            selectedReceiptIIndex.value = iindex;
            addDialog.value = true
        },
        onNameClick = { index -> selectedReceiptIndex.value = index; nameDialog.value = true },
        onChangeClick = { index, iindex ->
            selectedReceiptIndex.value = index;
            selectedReceiptIIndex.value = iindex;
            changeDialog.value = true
        }
    )

    if (addDialog.value) {
        Receipt_Add_Dialog(
            onDismiss = { addDialog.value = false },
            onConfirm = { newproductname, newprice, newquantity ->
                receipt.updateAddReceipt(
                    selectedReceiptIndex.value,
                    newproductname,
                    newprice,
                    newquantity
                )
                addDialog.value = false
            },
        )
    }

    if (nameDialog.value) {
        Receipt_Name_Dialog(
            onDismiss = { nameDialog.value = false },
            onConfirm = { newName ->
                receipt.updateReceiptName(selectedReceiptIndex.value, newName)
                nameDialog.value = false
            },
            name = receipts.value[selectedReceiptIndex.value].PlaceName
        )
    }

    if (changeDialog.value) {
        Receipt_Change_Dialog(
            onDismiss = { changeDialog.value = false },
            onConfirm = { newproductname, newprice, newquantity ->
                receipt.updateReceiptDetail(
                    selectedReceiptIndex.value,
                    selectedReceiptIIndex.value,
                    newproductname,
                    newprice,
                    newquantity
                )
                changeDialog.value = false
            },
            productName = receipts.value[selectedReceiptIndex.value].ProductName[selectedReceiptIIndex.value],
            price = receipts.value[selectedReceiptIndex.value].ProductQuantity[selectedReceiptIIndex.value],
            quantity = receipts.value[selectedReceiptIndex.value].ProductPrice[selectedReceiptIIndex.value]
        )
    }

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
    receipts: List<ReceiptClass>,
    receiptViewModel: Receipt,
    onAddClick: (Int, Int) -> Unit,
    onNameClick: (Int) -> Unit,
    onChangeClick: (Int, Int) -> Unit
) {
    val receiptlistState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val expandedStates = remember { mutableStateListOf<Boolean>() }

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
                val productPrices = receipts[index].ProductPrice
                val productQuantities = receipts[index].ProductQuantity

                val totalReceiptCost = receipts[index].ProductPrice
                    .zip(receipts[index].ProductQuantity) { price, quantity -> price.toInt() * quantity.toInt() }
                    .sum()


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
                                text = "${receipt.PlaceName} (${totalReceiptCost}원)",
                                modifier = Modifier.clickable { onNameClick(index) }
                            )

                            Elevated_Button(
                                content1 = "영수증 접기",
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

                            for (i in productPrices.indices) {
                                val productName = receipts[index].ProductName[i]
                                val price = receipts[index].ProductPrice[i]
                                val quantity = receipts[index].ProductQuantity[i]
                                val totalCost = (price.toInt() * quantity.toInt()).toString()
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onChangeClick(index, i) },
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
                                            text = totalCost,
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
                                    onClick = { onAddClick(index, productPrices.size - 1) }
                                )
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