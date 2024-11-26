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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.standardofsplit.data.model.ReceiptClass
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
    var showAddDialog by remember { mutableStateOf<Int?>(null) }
    var showNameDialog by remember { mutableStateOf<Int?>(null) }
    var showChangeDialog by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var showNewDialog by remember { mutableStateOf(false) }

    BackHandler { onBack() }

    // 상품 추가 다이얼로그
    showAddDialog?.let { index ->
        ProductAddDialog(
            onDismiss = { showAddDialog = null },
            onConfirm = { productName, price, quantity ->
                viewModel.addReceiptItem(
                    index = index,
                    productName = productName,
                    productQuantity = quantity,
                    productPrice = price
                )
                showAddDialog = null
            },
            onShowToast = { message ->
                if (!isToastShowing) {
                    isToastShowing = true
                    showCustomToast(context, message)
                    MainScope().launch {
                        delay(2000)
                        isToastShowing = false
                    }
                }
            }
        )
    }

    // 영수증 이름 변경 다이얼로그
    showNameDialog?.let { index ->
        Receipt_Name_Dialog(
            onDismiss = { showNameDialog = null },
            onConfirm = { newName ->
                viewModel.updateReceiptName(index, newName)
                showNameDialog = null
            },
            onDelete = {
                viewModel.deleteReceipt(index)
                showNameDialog = null
            },
            name = receipts[index].placeName
        )
    }

    // 상품 수정 다이얼로그
    showChangeDialog?.let { (receiptIndex, itemIndex) ->
        Receipt_Change_Dialog(
            onDismiss = { showChangeDialog = null },
            onConfirm = { productName, price, quantity ->
                viewModel.updateReceiptDetail(
                    index = receiptIndex,
                    itemIndex = itemIndex,
                    productName = productName,
                    productQuantity = quantity,
                    productPrice = price
                )
                showChangeDialog = null
            },
            onDelete = {
                viewModel.deleteReceiptItem(receiptIndex, itemIndex)
                showChangeDialog = null
            },
            productName = receipts[receiptIndex].productName[itemIndex],
            price = receipts[receiptIndex].productQuantity[itemIndex],
            quantity = receipts[receiptIndex].productPrice[itemIndex]
        )
    }

    // 새 영수증 추가 다이얼로그
    if (showNewDialog) {
        ReceiptAddDialog(
            onDismiss = { showNewDialog = false },
            onConfirm = { newName ->
                viewModel.addReceipt(
                    ReceiptClass(
                        placeName = newName,
                        productName = mutableListOf(),
                        productPrice = mutableListOf(),
                        productQuantity = mutableListOf()
                    )
                )
                showNewDialog = false
            },
            onShowToast = { message ->
                if (!isToastShowing) {
                    isToastShowing = true
                    showCustomToast(context, message)
                    MainScope().launch {
                        delay(2000)
                        isToastShowing = false
                    }
                }
            }
        )
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
                                modifier = Modifier.clickable { showNameDialog = index },
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
                                        onClick = { showChangeDialog = Pair(index, i) },
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
                                        onClick = { showAddDialog = index }
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
                        onClick = { showNewDialog = true }
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