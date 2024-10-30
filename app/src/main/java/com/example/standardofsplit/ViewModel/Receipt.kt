package com.example.standardofsplit.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.Model.ReceiptClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Receipt : ViewModel() {

    private val _receipts = MutableLiveData<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: LiveData<MutableList<ReceiptClass>> = _receipts

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

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

    fun onAddDialogOpen() { _showDialog.value = true }
    fun onAddDialogClose() { _showDialog.value = false }


}