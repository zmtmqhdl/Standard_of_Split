package com.example.standardofsplit.View.Screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
    BTN_Basic(content = "정산시작쓰", onClick = intentToCalculatorActivity)
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