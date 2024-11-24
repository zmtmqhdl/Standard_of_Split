package com.example.standardofsplit.presentation.state

sealed class ReceiptState {
    data object None : ReceiptState()
    data class Add(val receiptIndex: Int) : ReceiptState()
    data class Name(val receiptIndex: Int) : ReceiptState()
    data class Change(val receiptIndex: Int, val itemIndex: Int) : ReceiptState()
    data object New : ReceiptState()
}