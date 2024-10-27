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


    // fun receiptIncrement() {
//        _receiptCount.value = (_receiptCount.value ?: 0) + 1
//    }

    private val _receipts = MutableLiveData<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: LiveData<MutableList<ReceiptClass>> = _receipts

    init {
        val defaultReceipt = ReceiptClass(
            ReceiptNumber = 0,
            PlaceName = "영수증",
            ProductName = mutableListOf("상품"),
            ProductQuantity = mutableListOf("0"),
            ProductPrice = mutableListOf("0")
        )
        _receipts.value?.add(defaultReceipt)
    }

    fun addReceipt(receipt: ReceiptClass) {
        val currentList = _receipts.value?.toMutableList() ?: mutableListOf()
        currentList.add(receipt)
        _receipts.value = currentList
    }


}