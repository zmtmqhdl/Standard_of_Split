package com.example.standardofsplit.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.Model.ReceiptClass

class Receipt : ViewModel() {
//
//    private val _receiptCount = MutableLiveData(0)
//    val receiptCount: LiveData<Int> = _receiptCount

    private val _receipts = MutableLiveData<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: LiveData<MutableList<ReceiptClass>> = _receipts

//    fun receiptIncrement() {
//        _receiptCount.value = (_receiptCount.value ?: 0) + 1
//    }



    fun addReceipt(receipt: ReceiptClass) {
        val currentList = _receipts.value?.toMutableList() ?: mutableListOf()
        currentList.add(receipt)
        _receipts.value = currentList
    }
}