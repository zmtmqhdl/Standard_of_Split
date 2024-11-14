package com.example.standardofsplit.View.Screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.Model.ReceiptClass
import com.example.standardofsplit.View.Components.*
import com.example.standardofsplit.ViewModel.Receipt
import com.example.standardofsplit.ui.theme.DarkGray
import kotlinx.coroutines.launch

@Composable
fun ReceiptScreen(
    receipt: Receipt,
    onNext: () -> Unit
) {
    val receipts = receipt.receipts.observeAsState(initial = emptyList())
    val dialogStates = remember { DialogStates() }
    val selectedIndices = remember { SelectedIndices() }

    ReceiptContent(
        receipts = receipts.value,
        dialogStates = dialogStates,
        selectedIndices = selectedIndices,
        receipt = receipt,
        onNext = onNext
    )

    HandleDialogs(
        receipts = receipts.value,
        dialogStates = dialogStates,
        selectedIndices = selectedIndices,
        receipt = receipt
    )
}

private class DialogStates {
    var addDialog by mutableStateOf(false)
    var nameDialog by mutableStateOf(false)
    var changeDialog by mutableStateOf(false)
}

private class SelectedIndices {
    var receiptIndex by mutableStateOf(-1)
    var receiptIIndex by mutableStateOf(-1)
}

@Composable
private fun ReceiptContent(
    receipts: List<ReceiptClass>,
    dialogStates: DialogStates,
    selectedIndices: SelectedIndices,
    receipt: Receipt,
    onNext: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ReceiptList(
            receipts = receipts,
            dialogStates = dialogStates,
            selectedIndices = selectedIndices,
            receipt = receipt
        )

        NextButton(onClick = onNext)
    }
}

@Composable
private fun HandleDialogs(
    receipts: List<ReceiptClass>,
    dialogStates: DialogStates,
    selectedIndices: SelectedIndices,
    receipt: Receipt
) {
    if (dialogStates.addDialog) {
        Receipt_Add_Dialog(
            onDismiss = { dialogStates.addDialog = false },
            onConfirm = { newproductname, newprice, newquantity ->
                receipt.updateAddReceipt(
                    selectedIndices.receiptIndex,
                    newproductname,
                    newquantity,
                    newprice
                )
                dialogStates.addDialog = false
            }
        )
    }

    if (dialogStates.nameDialog) {
        Receipt_Name_Dialog(
            onDismiss = { dialogStates.nameDialog = false },
            onConfirm = { newName ->
                receipt.updateReceiptName(selectedIndices.receiptIndex, newName)
                dialogStates.nameDialog = false
            },
            name = receipts[selectedIndices.receiptIndex].PlaceName
        )
    }

    if (dialogStates.changeDialog) {
        Receipt_Change_Dialog(
            onDismiss = { dialogStates.changeDialog = false },
            onConfirm = { newproductname, newprice, newquantity ->
                receipt.updateReceiptDetail(
                    selectedIndices.receiptIndex,
                    selectedIndices.receiptIIndex,
                    newproductname,
                    newquantity,
                    newprice
                )
                dialogStates.changeDialog = false
            },
            productName = receipts[selectedIndices.receiptIndex].ProductName[selectedIndices.receiptIIndex],
            price = receipts[selectedIndices.receiptIndex].ProductQuantity[selectedIndices.receiptIIndex],
            quantity = receipts[selectedIndices.receiptIndex].ProductPrice[selectedIndices.receiptIIndex]
        )
    }
}

@Composable
private fun ReceiptList(
    receipts: List<ReceiptClass>,
    dialogStates: DialogStates,
    selectedIndices: SelectedIndices,
    receipt: Receipt
) {
    val receiptListState = rememberLazyListState()
    val expandedStates = remember { mutableStateListOf<Boolean>() }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = receiptListState,
        modifier = Modifier
            .fillMaxWidth()
            .height(800.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(receipts) { index, receipt ->
            expandedStates.add(false)
            val expanded = expandedStates[index]
            val totalReceiptCost = formatNumberWithCommas(
                receipts[index].ProductPrice.zip(receipts[index].ProductQuantity) { price, quantity ->
                    price.toInt() * quantity.toInt()
                }.sum().toString()
            )

            ReceiptCard(
                receipt = receipt,
                expanded = expanded,
                totalReceiptCost = totalReceiptCost,
                onNameClick = {
                    selectedIndices.receiptIndex = index
                    dialogStates.nameDialog = true
                },
                onExpandClick = { expandedStates[index] = !expanded },
                onItemClick = { iindex ->
                    selectedIndices.receiptIndex = index
                    selectedIndices.receiptIIndex = iindex
                    dialogStates.changeDialog = true
                },
                onAddClick = {
                    selectedIndices.receiptIndex = index
                    selectedIndices.receiptIIndex = receipts[index].ProductPrice.size - 1
                    dialogStates.addDialog = true
                }
            )
        }

        item {
            Basic_Button(
                content = "영수증 추가",
                onClick = {
                    val newReceipt = ReceiptClass(ReceiptNumber = receipts.size)
                    receipt.addReceipt(newReceipt)
                    coroutineScope.launch {
                        receiptListState.animateScrollToItem(receipts.size)
                    }
                }
            )
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
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkGray
        )
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(16.dp)
        ) {
            ReceiptHeader(
                placeName = receipt.PlaceName,
                totalCost = totalReceiptCost,
                expanded = expanded,
                onNameClick = onNameClick,
                onExpandClick = onExpandClick
            )

            if (expanded) {
                ReceiptDetails(
                    receipt = receipt,
                    onItemClick = onItemClick,
                    onAddClick = onAddClick
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

        Elevated_Button(
            content1 = "영수증 접기",
            content2 = "영수증 펼치기",
            flag = expanded,
            onClick = onExpandClick
        )
    }
}

@Composable
private fun ReceiptDetails(
    receipt: ReceiptClass,
    onItemClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    Column {
        Divider(modifier = Modifier.padding(top = 15.dp))
        ReceiptColumnHeaders()
        Divider()

        receipt.ProductPrice.indices.forEach { i ->
            ReceiptItem(
                productName = receipt.ProductName[i],
                price = receipt.ProductPrice[i],
                quantity = receipt.ProductQuantity[i],
                onClick = { onItemClick(i) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Basic_Button2 (
                content = "상품 추가",
                onClick = onAddClick
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
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 18.sp
        )
        Text(
            text = "단가 (수량)",
            modifier = Modifier.weight(2f),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 18.sp
        )
        Text(
            text = "금액",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun ReceiptItem(
    productName: String,
    price: String,
    quantity: String,
    onClick: () -> Unit
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

@Composable
private fun NextButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Basic_Button(
            content = "정산시작쓰",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = onClick
        )
    }
}