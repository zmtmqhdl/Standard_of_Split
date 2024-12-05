package com.example.standardofsplit.data.model

import kotlinx.coroutines.flow.MutableStateFlow

data class TotalPay(
    val payment: MutableStateFlow<MutableMap<Int, MutableMap<String, MutableMap<String, Int>>>> = MutableStateFlow(mutableMapOf())
)