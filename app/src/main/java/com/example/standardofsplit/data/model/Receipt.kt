package com.example.standardofsplit.data.model

import kotlinx.coroutines.flow.MutableStateFlow

data class ReceiptClass(
    var placeName: String = "영수증",
    val productName: MutableStateFlow<MutableList<String>> = MutableStateFlow(mutableListOf()),
    val productQuantity: MutableStateFlow<MutableList<Int>> = MutableStateFlow(mutableListOf()),
    val productPrice: MutableStateFlow<MutableList<Int>> = MutableStateFlow(mutableListOf()),
)