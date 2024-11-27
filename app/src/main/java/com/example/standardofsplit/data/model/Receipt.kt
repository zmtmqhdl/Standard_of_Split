package com.example.standardofsplit.data.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ReceiptClass(
    var placeName: String = "영수증",
    val productName: StateFlow<MutableList<String>> = MutableStateFlow(mutableListOf()),
    val productQuantity: StateFlow<MutableList<String>> = MutableStateFlow(mutableListOf()),
    val productPrice: StateFlow<MutableList<String>> = MutableStateFlow(mutableListOf()),
)