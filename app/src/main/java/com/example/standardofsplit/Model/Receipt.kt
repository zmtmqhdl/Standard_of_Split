package com.example.standardofsplit.Model

data class items(
    var ReceiptName: String,
    val MenuName: MutableList<String>,
    val MenuQuantity: MutableList<String>,
    val MenuPrice: MutableList<String>
)