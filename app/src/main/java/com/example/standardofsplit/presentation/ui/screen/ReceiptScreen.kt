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
import kotlinx.coroutines.flow.MutableStateFlow

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
private fun ProductList(
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
    val receiptViewModel: ReceiptViewModel = hiltViewModel()

    val receipts by receiptViewModel.receipts.collectAsState()
    val context = LocalContext.current

    var showReceiptAddDialog by remember { mutableStateOf(false) }
    var showReceiptNameUpdateDialog by remember { mutableStateOf<Int?>(null) }

    val expandedStates = remember { mutableStateListOf<Boolean>() }
    var isToastShowing by remember { mutableStateOf(false) }
    var showProductAddDialog by remember { mutableStateOf<Int?>(null) }
    var showProductUpdateDialog by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    BackHandler { onBack() }

    if (showReceiptAddDialog) {
        ReceiptAddDialog(
            onDismiss = { showReceiptAddDialog = false },
            onConfirm = { newName ->
                receiptViewModel.receiptAdd(
                    ReceiptClass(
                        placeName = newName,
                        productName = MutableStateFlow(mutableListOf()),
                        productPrice = MutableStateFlow(mutableListOf()),
                        productQuantity = MutableStateFlow(mutableListOf()),
                    )
                )
                showReceiptAddDialog = false
            },
            toastMessage = { message ->
                showCustomToast(context, message)
            }
        )
    }

    showReceiptNameUpdateDialog?.let { index ->
        ReceiptNameUpdateDialog(
            onDismiss = { showReceiptNameUpdateDialog = null },
            onConfirm = { newName ->
                receiptViewModel.receiptUpdate(index, newName)
                showReceiptNameUpdateDialog = null
            },
            onDelete = {
                receiptViewModel.receiptDelete(index)
                showReceiptNameUpdateDialog = null
            },
            toastMessage = { message ->
                showCustomToast(context, message)
            },
            name = receipts[index].placeName
        )
    }

    showProductAddDialog?.let { index ->
        ProductAddDialog(
            onDismiss = { showProductAddDialog = null },
            onConfirm = { name, price, quantity ->
                receiptViewModel.productAdd(
                    index = index,
                    productName = name,
                    productQuantity = quantity,
                    productPrice = price
                )
                showProductAddDialog = null
            },
            toastMessage = { message ->
                showCustomToast(context, message)
            }
        )
    }

    showProductUpdateDialog?.let { (receiptIndex, itemIndex) ->
        val productNames by receipts[receiptIndex].productName.collectAsState()
        val productQuantities by receipts[receiptIndex].productQuantity.collectAsState()
        val productPrices by receipts[receiptIndex].productPrice.collectAsState()

        ProductUpdateDialog(
            onDismiss = { showProductUpdateDialog = null },
            onConfirm = { productName, price, quantity ->
                receiptViewModel.productUpdate(
                    index = receiptIndex,
                    productIndex = itemIndex,
                    productName = productName,
                    productQuantity = quantity,
                    productPrice = price
                )
                showProductUpdateDialog = null
            },
            onDelete = {
                receiptViewModel.deleteReceiptItem(receiptIndex, itemIndex)
                showProductUpdateDialog = null
            },
            productName = productNames[itemIndex],  // 상품명
            price = productPrices[itemIndex],        // 가격
            quantity = productQuantities[itemIndex]
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
                            val productPrices by receipt.productPrice.collectAsState()
                            val productQuantities by receipt.productQuantity.collectAsState()
                            val totalCost = formatNumberWithCommas(
                                productPrices.zip(productQuantities) { price, quantity ->
                                    val priceInt = price.toIntOrNull() ?: 0
                                    val quantityInt = quantity.toIntOrNull() ?: 0
                                    priceInt * quantityInt
                                }.sum().toString()
                            )
                            Text(
                                text = "${receipt.placeName} (${totalCost}원)",
                                modifier = Modifier.clickable { showReceiptNameUpdateDialog = index },
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
                            val productNames by receipt.productName.collectAsState()
                            val productPrices by receipt.productPrice.collectAsState()
                            val productQuantities by receipt.productQuantity.collectAsState()
                            Column {
                                Divider(modifier = Modifier.padding(top = 15.dp))
                                ReceiptColumnHeaders()
                                Divider()
                                productPrices.indices.forEach { i ->
                                    ProductList(
                                        onClick = { showProductUpdateDialog = Pair(index, i) },
                                        productName = productNames[i],
                                        price = productPrices[i],
                                        quantity = productQuantities[i]
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
                                        onClick = { showProductAddDialog = index }
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
                        onClick = { showReceiptAddDialog = true }
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
                    val hasValidReceipt = receipts.any { receipt ->
                        receipt.productName.collectAsState().value.isNotEmpty()
                    }
                    if (hasValidReceipt) {
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