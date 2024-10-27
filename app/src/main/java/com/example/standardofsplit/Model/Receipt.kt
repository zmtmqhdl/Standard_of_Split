package com.example.standardofsplit.Model

data class ReceiptClass(
    var ReceiptNumber: Int,
    var PlaceName: String = "새로운 영수증",
    val ProductName: MutableList<String> = mutableListOf("상품"),
    val ProductQuantity: MutableList<String> = mutableListOf("0"),
    val ProductPrice: MutableList<String> = mutableListOf("0"),
)