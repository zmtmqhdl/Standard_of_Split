package com.example.standardofsplit.View.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.standardofsplit.Model.ReceiptClass
import com.example.standardofsplit.View.Activity.CalculatorActivity
import com.example.standardofsplit.View.Components.BTN_Basic
import com.example.standardofsplit.ViewModel.Receipt

@Composable
fun ReceiptScreen(
    receipt: Receipt,
    intentToCalculatorActivity: () -> Unit
) {
    val receipts = receipt.receipts.observeAsState(initial = emptyList())

    ReceiptDetailList(receipts = receipts.value)

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
) {
    for ((index, receipt) in receipts.withIndex()) {
        // 각 영수증에 대해 버튼 생성
        ReceiptDetailButton(
            receipt = receipt,
            index = index,
            onClick = { }
        )
    }
}

@Composable
fun ReceiptDetailButton(
    receipt: ReceiptClass,
    index: Int,
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