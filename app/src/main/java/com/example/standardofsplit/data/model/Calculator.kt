package com.example.standardofsplit.data.model

data class TotalPay(
    val payments: MutableMap<Int, MutableMap<String, MutableMap<String, Int>>> = mutableMapOf()
)