package com.example.standardofsplit.presentation.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.standardofsplit.data.model.ReceiptClass
import com.example.standardofsplit.presentation.event.ReceiptEvent
import com.example.standardofsplit.presentation.state.ReceiptState
import com.example.standardofsplit.presentation.ui.component.*
import com.example.standardofsplit.presentation.ui.theme.Typography
import com.example.standardofsplit.presentation.viewModel.ReceiptViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
private fun ReceiptColumnHeaders() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        Text(
            text = "상품명",
            modifier = Modifier.weight(1f),
            style = Typography.receiptColumnHeaderTextStyle,
            textAlign = TextAlign.Left
        )
        Text(
            text = "단가 (수량)",
            modifier = Modifier.weight(1f),
            style = Typography.receiptColumnHeaderTextStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = "금액",
            modifier = Modifier.weight(1f),
            style = Typography.receiptColumnHeaderTextStyle,
            textAlign = TextAlign.Right
        )
    }
}

@Composable
private fun ReceiptItem(
    onClick: () -> Unit,
    productName: String,
    price: String,
    quantity: String,
) {
    val totalCost = (price.toInt() * quantity.toInt()).toString()
    val formattedPrice = formatNumberWithCommas(price)
    val formattedTotalCost = formatNumberWithCommas(totalCost)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                style = Typography.receiptItemTextStyle,
                textAlign = TextAlign.Left
            )
            Text(
                text = "$formattedPrice ($quantity)",
                modifier = Modifier.weight(1f),
                style = Typography.receiptItemTextStyle,
                textAlign = TextAlign.Center
            )
            Text(
                text = formattedTotalCost,
                modifier = Modifier.weight(1f),
                style = Typography.receiptItemTextStyle,
                textAlign = TextAlign.Right
            )
        }
    }
}


@Composable
fun ReceiptScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val viewModel: ReceiptViewModel = hiltViewModel()
    val receipts by viewModel.receipts.collectAsState()
    val context = LocalContext.current
    val expandedStates = remember { mutableStateListOf<Boolean>() }
    var isToastShowing by remember { mutableStateOf(false) }
    var dialogState by remember { mutableStateOf<ReceiptState>(ReceiptState.None) }

    BackHandler { onBack() }

    when (dialogState) {
        is ReceiptState.Add -> {
            val state = dialogState as ReceiptState.Add
            Receipt_Add_Dialog(
                onDismiss = { dialogState = ReceiptState.None },
                onConfirm = { productName, price, quantity ->
                    viewModel.onEvent(ReceiptEvent.AddReceiptItem(
                        index = state.receiptIndex,
                        productName = productName,
                        productQuantity = quantity,
                        productPrice = price
                    ))
                    dialogState = ReceiptState.None
                }
            )
        }
        is ReceiptState.Name -> {
            val state = dialogState as ReceiptState.Name
            Receipt_Name_Dialog(
                onDismiss = { dialogState = ReceiptState.None },
                onConfirm = { newName ->
                    viewModel.onEvent(ReceiptEvent.UpdateReceiptName(
                        index = state.receiptIndex,
                        newName = newName
                    ))
                    dialogState = ReceiptState.None
                },
                onDelete = {
                    viewModel.onEvent(ReceiptEvent.DeleteReceipt(state.receiptIndex))
                    dialogState = ReceiptState.None
                },
                name = receipts[state.receiptIndex].placeName
            )
        }
        is ReceiptState.Change -> {
            val state = dialogState as ReceiptState.Change
            Receipt_Change_Dialog(
                onDismiss = { dialogState = ReceiptState.None },
                onConfirm = { productName, price, quantity ->
                    viewModel.onEvent(ReceiptEvent.UpdateReceiptDetail(
                        index = state.receiptIndex,
                        itemIndex = state.itemIndex,
                        productName = productName,
                        productQuantity = quantity,
                        productPrice = price
                    ))
                    dialogState = ReceiptState.None
                },
                onDelete = {
                    viewModel.onEvent(ReceiptEvent.DeleteReceiptItem(
                        receiptIndex = state.receiptIndex,
                        itemIndex = state.itemIndex
                    ))
                    dialogState = ReceiptState.None
                },
                productName = receipts[state.receiptIndex].productName[state.itemIndex],
                price = receipts[state.receiptIndex].productQuantity[state.itemIndex],
                quantity = receipts[state.receiptIndex].productPrice[state.itemIndex]
            )
        }
        is ReceiptState.New -> {
            Receipt_New_Dialog(
                onDismiss = { dialogState = ReceiptState.None },
                onConfirm = { newName ->
                    viewModel.onEvent(ReceiptEvent.AddReceipt(
                        ReceiptClass(
                            placeName = newName,
                            productName = mutableListOf(),
                            productPrice = mutableListOf(),
                            productQuantity = mutableListOf()
                        )
                    ))
                    dialogState = ReceiptState.None
                }
            )
        }
        ReceiptState.None -> { /* 다이얼로그 닫힘 */ }
    }

    val onNameClick = { index: Int ->
        dialogState = ReceiptState.Name(index)
    }

    val onChangeClick = { receiptIndex: Int, itemIndex: Int ->
        dialogState = ReceiptState.Change(receiptIndex, itemIndex)
    }

    val onAddClick = { index: Int ->
        dialogState = ReceiptState.Add(index)
    }

    val onNewClick = {
        dialogState = ReceiptState.New
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(receipts) { index, receipt ->
                if (expandedStates.size <= index) {
                    expandedStates.add(false)
                }
                Card(
                    modifier = Modifier
                        .width(420.dp)
                        .wrapContentHeight()
                        .padding(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
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
                            val totalCost = formatNumberWithCommas(
                                receipt.productPrice.zip(receipt.productQuantity) { price, quantity ->
                                    price.toInt() * quantity.toInt()
                                }.sum().toString()
                            )
                            Text(
                                text = "${receipt.placeName} (${totalCost}원)",
                                modifier = Modifier.clickable { onNameClick(index) },
                                style = Typography.receiptHeadTextStyle
                            )
                            ReceiptOpenCloseButton(
                                text1 = "영수증 접기",
                                text2 = "영수증 펼치기",
                                onClick = { expandedStates[index] = !expandedStates[index] },
                                flag = expandedStates[index]
                            )
                        }
                        if (expandedStates[index]) {
                            Column {
                                Divider(modifier = Modifier.padding(top = 15.dp))
                                ReceiptColumnHeaders()
                                Divider()
                                receipt.productPrice.indices.forEach { i ->
                                    ReceiptItem(
                                        onClick = { onChangeClick(index, i) },
                                        productName = receipt.productName[i],
                                        price = receipt.productPrice[i],
                                        quantity = receipt.productQuantity[i]
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AddButton(
                                        text = "상품 추가",
                                        onClick = { onAddClick(index) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AddButton(
                        text = "영수증 추가",
                        onClick = onNewClick
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
        ) {
            SubmitButton(
                text = "정산 시작",
                onClick = {
                    viewModel.onEvent(ReceiptEvent.CheckReceipts)
                    if (receipts.any { it.productName.isNotEmpty() }) {
                        onNext()
                    } else if (!isToastShowing) {
                        isToastShowing = true
                        showCustomToast(context, "최소 1개 이상의 상품이 포함된 영수증이 1개 이상 필요합니다.")
                        MainScope().launch {
                            delay(2000)
                            isToastShowing = false
                        }
                    }
                }
            )
        }
    }
}