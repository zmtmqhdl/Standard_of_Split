package com.example.standardofsplit.Model

data class ReceiptClass(
    var ReceiptNumber: Int,
    var PlaceName: String,
    val ProductName: MutableList<String>,
    val ProductQuantity: MutableList<String>,
    val ProductPrice: MutableList<String>
)