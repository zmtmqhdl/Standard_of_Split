package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReceiptCount: ViewModel() {
    private val receiptCount = MutableLiveData(1)
    val count: LiveData<Int> = receiptCount

    fun increment() {
        receiptCount.value = (receiptCount.value ?: 1) + 1
    }
}