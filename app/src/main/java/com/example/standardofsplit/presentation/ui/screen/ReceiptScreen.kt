package com.example.standardofsplit.presentation.ui.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.standardofsplit.data.model.ReceiptClass
import com.example.standardofsplit.presentation.ui.component.AddButton
import com.example.standardofsplit.presentation.ui.component.ProductAddDialog
import com.example.standardofsplit.presentation.ui.component.ProductUpdateDialog
import com.example.standardofsplit.presentation.ui.component.ReceiptAddDialog
import com.example.standardofsplit.presentation.ui.component.ReceiptNameUpdateDialog
import com.example.standardofsplit.presentation.ui.component.ReceiptOpenCloseButton
import com.example.standardofsplit.presentation.ui.component.SubmitButton
import com.example.standardofsplit.presentation.ui.component.formatNumberWithCommas
import com.example.standardofsplit.presentation.ui.component.showCustomToast
import com.example.standardofsplit.presentation.ui.theme.Typography
import com.example.standardofsplit.presentation.viewModel.ReceiptViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.String.format

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
    price: Int,
    quantity: Int,
) {
    val totalCost = (price * quantity).toString()
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

    showProductUpdateDialog?.let { (index, itemIndex) ->
        ProductUpdateDialog(
            onDismiss = { showProductUpdateDialog = null },
            onConfirm = { productName, price, quantity ->
                receiptViewModel.productUpdate(
                    index = index,
                    productIndex = itemIndex,
                    productName = productName,
                    productQuantity = quantity,
                    productPrice = price
                )
                showProductUpdateDialog = null
            },
            onDelete = {
                receiptViewModel.deleteReceiptItem(index, itemIndex)
                showProductUpdateDialog = null
            },
            productName = receipts[index].productName.value[itemIndex],
            price = receipts[index].productPrice.value[itemIndex],
            quantity = receipts[index].productQuantity.value[itemIndex]
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
                                    price * quantity
                                }.sum().toString()
                            )
                            Text(
                                text = "${receipt.placeName} (${totalCost}원)",
                                modifier = Modifier.clickable {
                                    showReceiptNameUpdateDialog = index
                                },
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
                                HorizontalDivider(modifier = Modifier.padding(top = 15.dp))
                                ReceiptColumnHeaders()
                                HorizontalDivider()
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
                    receiptViewModel.receiptCheckAndNext(
                        onNext = onNext,
                        context = context
                    )
                }
            )
        }
    }
}

@SuppressLint("DefaultLocale")
private fun formatNumberWithCommas(number: Int): Int {
    return format("%,d", number).toInt()
}