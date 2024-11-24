package com.example.standardofsplit.data.model

data class ReceiptClass(
    var placeName: String = "영수증",
    val productName: MutableList<String> = mutableListOf(),
    val productQuantity: MutableList<String> = mutableListOf(),
    val productPrice: MutableList<String> = mutableListOf(),
)