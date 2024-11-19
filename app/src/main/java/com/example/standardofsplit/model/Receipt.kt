package com.example.standardofsplit.model

data class ReceiptClass(
    var ReceiptNumber: Int,
    var PlaceName: String = "영수증",
    val ProductName: MutableList<String> = mutableListOf(),
    val ProductQuantity: MutableList<String> = mutableListOf(),
    val ProductPrice: MutableList<String> = mutableListOf(),
)