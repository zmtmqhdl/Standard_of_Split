package com.example.standardofsplit.View.Screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.Model.ReceiptClass
import com.example.standardofsplit.View.Components.*
import com.example.standardofsplit.ViewModel.Receipt
import com.example.standardofsplit.ui.theme.DarkGray
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun ReceiptScreen(
    receipt: Receipt, onNext: () -> Unit
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
    var newDialog by mutableStateOf(false)
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
                    receipt = receipt
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 50.dp), contentAlignment = Alignment.Center
        ) {
            NextButton(
                onClick = {
                    if (receipt.check()) {
                        onNext()
                    } else if (!isToastShowing) {
                        isToastShowing = true
                        showCustomToast(
                            context = context,
                            message = "최소 1개 이상의 상품이 포함된 영수증이 1개 이상  필요합니다."
                        )
                        MainScope().launch {
                            kotlinx.coroutines.delay(3000)
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
    receipt: Receipt
) {
    if (dialogStates.addDialog) {
        Receipt_Add_Dialog(onDismiss = { dialogStates.addDialog = false },
            onConfirm = { newproductname, newprice, newquantity ->
                receipt.updateAddReceipt(
                    selectedIndices.receiptIndex, newproductname, newquantity, newprice
                )
                dialogStates.addDialog = false
            })
    }

    if (dialogStates.nameDialog) {
        Receipt_Name_Dialog(onDismiss = { dialogStates.nameDialog = false },
            onConfirm = { newName ->
                receipt.updateReceiptName(selectedIndices.receiptIndex, newName)
                dialogStates.nameDialog = false
            },
            onDelete = {
                receipt.deleteReceipt(selectedIndices.receiptIndex)
                dialogStates.nameDialog = false
            },
            name = receipts[selectedIndices.receiptIndex].PlaceName
        )
    }

    if (dialogStates.changeDialog) {
        Receipt_Change_Dialog(onDismiss = { dialogStates.changeDialog = false },
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
            onDelete = {
                receipt.deleteReceiptItem(
                    selectedIndices.receiptIndex, selectedIndices.receiptIIndex
                )
                dialogStates.changeDialog = false
            },
            productName = receipts[selectedIndices.receiptIndex].ProductName[selectedIndices.receiptIIndex],
            price = receipts[selectedIndices.receiptIndex].ProductQuantity[selectedIndices.receiptIIndex],
            quantity = receipts[selectedIndices.receiptIndex].ProductPrice[selectedIndices.receiptIIndex]
        )
    }

    if (dialogStates.newDialog) {
        Receipt_New_Dialog(
            onDismiss = { dialogStates.newDialog = false },
            onConfirm = { newName ->
                val newReceipt = ReceiptClass(ReceiptNumber = receipts.size)
                newReceipt.PlaceName = newName
                receipt.addReceipt(newReceipt)
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
    receipt: Receipt
) {
    val expandedStates = remember { mutableStateListOf<Boolean>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp),
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

            ReceiptCard(receipt = receipt,
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
                })
        }

        item {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                add_Button(
                    content = "영수증 추가",
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

        Elevated_Button(
            content1 = "영수증 접기", content2 = "영수증 펼치기", flag = expanded, onClick = onExpandClick
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

        receipt.ProductPrice.indices.forEach { i ->
            ReceiptItem(productName = receipt.ProductName[i],
                price = receipt.ProductPrice[i],
                quantity = receipt.ProductQuantity[i],
                onClick = { onItemClick(i) })
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Basic_Button2(
                content = "상품 추가", onClick = onAddClick
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

@Composable
private fun NextButton(onClick: () -> Unit) {
    Basic_Button(
        content = "정산 시작",
        onClick = onClick,
    )
}
