package com.example.standardofsplit.presentation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.model.ReceiptClass

class Receipt : ViewModel() {
    private val _receipts = MutableLiveData<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: LiveData<MutableList<ReceiptClass>> = _receipts

    companion object {
        private val DEFAULT_RECEIPT = ReceiptClass(
            ReceiptNumber = 0,
            PlaceName = "영수증",
            ProductName = mutableListOf(),
            ProductPrice = mutableListOf(),
            ProductQuantity = mutableListOf()
        )
    }

    init {
        _receipts.value?.add(DEFAULT_RECEIPT)
    }

    fun addReceipt(receipt: ReceiptClass) {
        _receipts.value = (_receipts.value?.toMutableList() ?: mutableListOf()).apply {
            add(receipt)
        }
    }

    fun updateReceiptName(index: Int, newName: String) {
        _receipts.value = _receipts.value?.toMutableList()?.apply {
            this[index].PlaceName = newName
        }
    }

    fun updateReceiptDetail(
        index: Int,
        iindex: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        _receipts.value = _receipts.value?.toMutableList()?.apply {
            this[index].apply {
                ProductName[iindex] = productName
                ProductQuantity[iindex] = productQuantity
                ProductPrice[iindex] = productPrice
            }
        }
    }

    fun updateAddReceipt(
        index: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        _receipts.value = _receipts.value?.toMutableList()?.apply {
            this[index].apply {
                ProductName.add(productName)
                ProductQuantity.add(productQuantity)
                ProductPrice.add(productPrice)
            }
        }
    }

    fun deleteReceipt(index: Int) {
        val currentList = _receipts.value?.toMutableList() ?: mutableListOf()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _receipts.value = currentList
        }
    }

    fun deleteReceiptItem(receiptIndex: Int, itemIndex: Int) {
        val currentList = _receipts.value?.toMutableList() ?: mutableListOf()
        if (receiptIndex in currentList.indices) {
            val receipt = currentList[receiptIndex]
            receipt.ProductName.removeAt(itemIndex)
            receipt.ProductPrice.removeAt(itemIndex)
            receipt.ProductQuantity.removeAt(itemIndex)
            _receipts.value = currentList
        }
    }

    fun check(): Boolean {
        val currentList = _receipts.value?.toMutableList() ?: return false
        val hasValidReceipt = currentList.any { receipt ->
            receipt.ProductName.isNotEmpty()
        }
        if (!hasValidReceipt) {
            return false
        }
        val emptyReceiptIndices = currentList.indices
            .filter { index -> currentList[index].ProductName.isEmpty() }
            .sortedDescending()
        emptyReceiptIndices.forEach { index ->
            deleteReceipt(index)
        }
        return true
    }

}