package com.example.standardofsplit.presentation.event

import com.example.standardofsplit.data.model.ReceiptClass

sealed class ReceiptEvent {
    data class AddReceipt(val receipt: ReceiptClass) : ReceiptEvent()
    data class UpdateReceiptName(val index: Int, val newName: String) : ReceiptEvent()
    data class UpdateReceiptDetail(
        val index: Int,
        val itemIndex: Int,
        val productName: String,
        val productQuantity: String,
        val productPrice: String
    ) : ReceiptEvent()
    data class AddReceiptItem(
        val index: Int,
        val productName: String,
        val productQuantity: String,
        val productPrice: String
    ) : ReceiptEvent()
    data class DeleteReceipt(val index: Int) : ReceiptEvent()
    data class DeleteReceiptItem(val receiptIndex: Int, val itemIndex: Int) : ReceiptEvent()
    object CheckReceipts : ReceiptEvent()
}