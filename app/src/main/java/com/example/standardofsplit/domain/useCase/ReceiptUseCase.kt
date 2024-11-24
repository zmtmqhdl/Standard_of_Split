package com.example.standardofsplit.domain.useCase

import com.example.standardofsplit.data.model.ReceiptClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ReceiptUseCase @Inject constructor() {
    private val _receipts = MutableStateFlow<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: StateFlow<MutableList<ReceiptClass>> = _receipts.asStateFlow()

    init {
        _receipts.value = mutableListOf(DEFAULT_RECEIPT)
    }

    fun addReceipt(receipt: ReceiptClass) {
        _receipts.value = (_receipts.value.toMutableList()).apply {
            add(receipt)
        }
    }

    fun updateReceiptName(index: Int, newName: String) {
        _receipts.value = _receipts.value.toMutableList().apply {
            this[index].placeName = newName
        }
    }

    fun updateReceiptDetail(
        index: Int,
        itemIndex: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        _receipts.value = _receipts.value.toMutableList().apply {
            this[index].apply {
                this.productName[itemIndex] = productName
                this.productQuantity[itemIndex] = productQuantity
                this.productPrice[itemIndex] = productPrice
            }
        }
    }

    fun addReceiptItem(
        index: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        _receipts.value = _receipts.value.toMutableList().apply {
            this[index].apply {
                this.productName.add(productName)
                this.productQuantity.add(productQuantity)
                this.productPrice.add(productPrice)
            }
        }
    }

    fun deleteReceipt(index: Int): Boolean {
        val currentList = _receipts.value.toMutableList()
        return if (index in currentList.indices) {
            currentList.removeAt(index)
            _receipts.value = currentList
            true
        } else false
    }

    fun deleteReceiptItem(receiptIndex: Int, itemIndex: Int): Boolean {
        val currentList = _receipts.value.toMutableList()
        return if (receiptIndex in currentList.indices) {
            val receipt = currentList[receiptIndex]
            receipt.productName.removeAt(itemIndex)
            receipt.productPrice.removeAt(itemIndex)
            receipt.productQuantity.removeAt(itemIndex)
            _receipts.value = currentList
            true
        } else false
    }

    fun validateAndCleanReceipts(): Boolean {
        val currentList = _receipts.value.toMutableList()
        val hasValidReceipt = currentList.any { receipt ->
            receipt.productName.isNotEmpty()
        }
        if (!hasValidReceipt) return false

        val emptyReceiptIndices = currentList.indices
            .filter { index -> currentList[index].productName.isEmpty() }
            .sortedDescending()
        
        emptyReceiptIndices.forEach { index ->
            deleteReceipt(index)
        }
        return true
    }

    companion object {
        private val DEFAULT_RECEIPT = ReceiptClass(
            placeName = "영수증",
            productName = mutableListOf(),
            productPrice = mutableListOf(),
            productQuantity = mutableListOf()
        )
    }
}