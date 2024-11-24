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
import com.example.standardofsplit.presentation.ui.component.*
import com.example.standardofsplit.presentation.ui.theme.Typography
import com.example.standardofsplit.presentation.viewModel.ReceiptViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun ReceiptScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    val receipts by viewModel.receipts.collectAsState()
    val dialogStates = remember { DialogStates() }
    val selectedIndices = remember { SelectedIndices() }

    BackHandler {
        onBack()
    }

    ReceiptContent(
        receipts = receipts,
        dialogStates = dialogStates,
        selectedIndices = selectedIndices,
        onEvent = viewModel::onEvent,
        onNext = onNext
    )

    HandleDialogs(
        receipts = receipts,
        dialogStates = dialogStates,
        selectedIndices = selectedIndices,
        onEvent = viewModel::onEvent
    )
}

private class DialogStates {
    var addDialog by mutableStateOf(false)
    var nameDialog by mutableStateOf(false)
    var changeDialog by mutableStateOf(false)
    var newDialog by mutableStateOf(false)
}

private class SelectedIndices {
    var selectedReceiptIndex by mutableStateOf(-1)
    var selectedItemIndex by mutableStateOf(-1)
}

@Composable
private fun ReceiptContent(
    receipts: List<ReceiptClass>,
    dialogStates: DialogStates,
    selectedIndices: SelectedIndices,
    onEvent: (ReceiptEvent) -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    var isToastShowing by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(760.dp)
            ) {
                ReceiptList(
                    receipts = receipts,
                    dialogStates = dialogStates,
                    selectedIndices = selectedIndices,
                    onEvent = onEvent
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 50.dp), contentAlignment = Alignment.Center
        ) {
            SubmitButton(
                text = "정산 시작",
                onClick = {
                    onEvent(ReceiptEvent.CheckReceipts)
                    if (receipts.any { it.productName.isNotEmpty() }) {
                        onNext()
                    } else if (!isToastShowing) {
                        isToastShowing = true
                        showCustomToast(
                            context = context,
                            message = "최소 1개 이상의 상품이 포함된 영수증이 1개 이상 필요합니다."
                        )
                        MainScope().launch {
                            kotlinx.coroutines.delay(2000)
                            isToastShowing = false
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun HandleDialogs(
    receipts: List<ReceiptClass>,
    dialogStates: DialogStates,
    selectedIndices: SelectedIndices,
    onEvent: (ReceiptEvent) -> Unit
) {
    if (dialogStates.addDialog) {
        Receipt_Add_Dialog(
            onDismiss = { dialogStates.addDialog = false },
            onConfirm = { productName, price, quantity ->
                onEvent(ReceiptEvent.AddReceiptItem(
                    index = selectedIndices.selectedReceiptIndex,
                    productName = productName,
                    productQuantity = quantity,
                    productPrice = price
                ))
                dialogStates.addDialog = false
            }
        )
    }

    if (dialogStates.nameDialog) {
        Receipt_Name_Dialog(
            onDismiss = { dialogStates.nameDialog = false },
            onConfirm = { newName ->
                onEvent(ReceiptEvent.UpdateReceiptName(
                    index = selectedIndices.selectedReceiptIndex,
                    newName = newName
                ))
                dialogStates.nameDialog = false
            },
            onDelete = {
                onEvent(ReceiptEvent.DeleteReceipt(selectedIndices.selectedReceiptIndex))
                dialogStates.nameDialog = false
            },
            name = receipts[selectedIndices.selectedReceiptIndex].placeName
        )
    }

    if (dialogStates.changeDialog) {
        Receipt_Change_Dialog(
            onDismiss = { dialogStates.changeDialog = false },
            onConfirm = { productName, price, quantity ->
                onEvent(ReceiptEvent.UpdateReceiptDetail(
                    index = selectedIndices.selectedReceiptIndex,
                    itemIndex = selectedIndices.selectedItemIndex,
                    productName = productName,
                    productQuantity = quantity,
                    productPrice = price
                ))
                dialogStates.changeDialog = false
            },
            onDelete = {
                onEvent(ReceiptEvent.DeleteReceiptItem(
                    receiptIndex = selectedIndices.selectedReceiptIndex,
                    itemIndex = selectedIndices.selectedItemIndex
                ))
                dialogStates.changeDialog = false
            },
            productName = receipts[selectedIndices.selectedReceiptIndex].productName[selectedIndices.selectedItemIndex],
            price = receipts[selectedIndices.selectedReceiptIndex].productQuantity[selectedIndices.selectedItemIndex],
            quantity = receipts[selectedIndices.selectedReceiptIndex].productPrice[selectedIndices.selectedItemIndex]
        )
    }

    if (dialogStates.newDialog) {
        Receipt_New_Dialog(
            onDismiss = { dialogStates.newDialog = false },
            onConfirm = { newName ->
                val newReceipt = ReceiptClass(
                    placeName = newName,
                    productName = mutableListOf(),
                    productPrice = mutableListOf(),
                    productQuantity = mutableListOf()
                )
                onEvent(ReceiptEvent.AddReceipt(newReceipt))
                dialogStates.newDialog = false
            }
        )
    }
}

@Composable
private fun ReceiptList(
    receipts: List<ReceiptClass>,
    dialogStates: DialogStates,
    selectedIndices: SelectedIndices,
    onEvent: (ReceiptEvent) -> Unit
) {
    val expandedStates = remember { mutableStateListOf<Boolean>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(receipts) { receiptIndex, receipt ->
            expandedStates.add(false)
            val isExpanded = expandedStates[receiptIndex]
            val totalReceiptCost = formatNumberWithCommas(
                receipts[receiptIndex].productPrice.zip(receipts[receiptIndex].productQuantity) { price, quantity ->
                    price.toInt() * quantity.toInt()
                }.sum().toString()
            )

            ReceiptCard(
                receipt = receipt,
                expanded = isExpanded,
                totalReceiptCost = totalReceiptCost,
                onNameClick = {
                    selectedIndices.selectedReceiptIndex = receiptIndex
                    dialogStates.nameDialog = true
                },
                onExpandClick = { expandedStates[receiptIndex] = !isExpanded },
                onItemClick = { itemIndex ->
                    selectedIndices.selectedReceiptIndex = receiptIndex
                    selectedIndices.selectedItemIndex = itemIndex
                    dialogStates.changeDialog = true
                },
                onAddClick = {
                    selectedIndices.selectedReceiptIndex = receiptIndex
                    selectedIndices.selectedItemIndex = receipts[receiptIndex].productPrice.size - 1
                    dialogStates.addDialog = true
                }
            )
        }

        item {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                ReceiptAddButton(
                    text = "영수증 추가",
                    onClick = { dialogStates.newDialog = true }
                )
            }
        }
    }
}

@Composable
private fun ReceiptCard(
    receipt: ReceiptClass,
    expanded: Boolean,
    totalReceiptCost: String,
    onNameClick: () -> Unit,
    onExpandClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(420.dp)
            .wrapContentHeight()
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray
        )
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(16.dp)
        ) {
            ReceiptHeader(
                placeName = receipt.placeName,
                totalCost = totalReceiptCost,
                expanded = expanded,
                onNameClick = onNameClick,
                onExpandClick = onExpandClick
            )

            if (expanded) {
                ReceiptDetails(
                    receipt = receipt, onItemClick = onItemClick, onAddClick = onAddClick
                )
            }
        }
    }
}

@Composable
private fun ReceiptHeader(
    placeName: String,
    totalCost: String,
    expanded: Boolean,
    onNameClick: () -> Unit,
    onExpandClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$placeName (${totalCost}원)",
            modifier = Modifier.clickable(onClick = onNameClick),
            color = Color.White,
            fontSize = 18.sp
        )

        ReceiptOpenCloseButton(
            text1 = "영수증 접기", text2 = "영수증 펼치기", flag = expanded, onClick = onExpandClick
        )
    }
}

@Composable
private fun ReceiptDetails(
    receipt: ReceiptClass, onItemClick: (Int) -> Unit, onAddClick: () -> Unit
) {
    Column {
        Divider(modifier = Modifier.padding(top = 15.dp))
        ReceiptColumnHeaders()
        Divider()

        receipt.productPrice.indices.forEach { i ->
            ReceiptItem(productName = receipt.productName[i],
                price = receipt.productPrice[i],
                quantity = receipt.productQuantity[i],
                onClick = { onItemClick(i) })
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            ProductAddButton(
                text = "상품 추가", onClick = onAddClick
            )
        }
    }
}

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
            style = Typography.ReceiptColumnHeaderTextStyle
        )
        Text(
            text = "단가 (수량)",
            modifier = Modifier.weight(2f),
            style = Typography.ReceiptColumnHeaderTextStyle
        )
        Text(
            text = "금액",
            modifier = Modifier.weight(1f),
            style = Typography.ReceiptColumnHeaderTextStyle
        )
    }
}

@Composable
private fun ReceiptItem(
    productName: String, price: String, quantity: String, onClick: () -> Unit
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
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = "$formattedPrice ($quantity)",
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = formattedTotalCost,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}