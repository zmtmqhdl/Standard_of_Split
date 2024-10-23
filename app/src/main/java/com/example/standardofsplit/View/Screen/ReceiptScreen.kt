package com.example.standardofsplit.View.Screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.standardofsplit.Model.ReceiptClass
import com.example.standardofsplit.View.Components.BTN_Basic
import com.example.standardofsplit.ViewModel.Receipt

@Composable
fun ReceiptScreen(
    receipt: Receipt,
    intentToCalculatorActivity: () -> Unit
) {
    val receipts = receipt.receipts.observeAsState(initial = emptyList())

    ReceiptDetailList(receipts = receipts.value, receiptViewModel = receipt)

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
    receiptViewModel: Receipt
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        for (receipt in receipts) {
            ReceiptDetailButton(
                receipt = receipt,
                onClick = { receiptViewModel.addReceipt(receipt) }
            )
        }
        BTN_Basic(
            content = "영수증 추가",
            onClick = {
                val newReceipt = ReceiptClass(
                    ReceiptNumber = receipts.size + 1,
                    PlaceName = "New Place",
                    ProductName = mutableListOf("Product 1"),
                    ProductQuantity = mutableListOf("1"),
                    ProductPrice = mutableListOf("100")
                )
                receiptViewModel.addReceipt(newReceipt)
            }
        )
    }
}

@Composable
fun ReceiptDetailButton(
    receipt: ReceiptClass,
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