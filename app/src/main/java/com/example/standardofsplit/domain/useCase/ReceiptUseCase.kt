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

    fun receiptAdd(receipt: ReceiptClass) {
        _receipts.value = (_receipts.value.toMutableList()).apply {
            add(receipt)
        }
    }

    fun receiptUpdate(index: Int, newName: String) {
        _receipts.value = _receipts.value.toMutableList().apply {
            this[index].placeName = newName
        }
    }

    fun receiptDelete(index: Int): Boolean {
        val currentList = _receipts.value.toMutableList()
        return if (index in currentList.indices) {
            currentList.removeAt(index)
            _receipts.value = currentList
            true
        } else false
    }

    fun productAdd(
        index: Int, productName: String, productQuantity: String, productPrice: String
    ) {
        _receipts.value = _receipts.value.toMutableList().apply {
            this[index].apply {
                val currentProductName = this.productName.value.toMutableList()
                val currentProductQuantity = this.productQuantity.value.toMutableList()
                val currentProductPrice = this.productPrice.value.toMutableList()
                currentProductName.add(productName)
                currentProductQuantity.add(productQuantity)
                currentProductPrice.add(productPrice)
                this.productName.value = currentProductName
                this.productQuantity.value = currentProductQuantity
                this.productPrice.value = currentProductPrice
            }
        }
    }

    fun productUpdate(
        index: Int,
        itemIndex: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        _receipts.value = _receipts.value.toMutableList().apply {
            this[index].apply {
                val currentProductName = this.productName.value.toMutableList()
                val currentProductQuantity = this.productQuantity.value.toMutableList()
                val currentProductPrice = this.productPrice.value.toMutableList()
                currentProductName[itemIndex] = productName
                currentProductQuantity[itemIndex] = productQuantity
                currentProductPrice[itemIndex] = productPrice
                this.productName.value = currentProductName
                this.productQuantity.value = currentProductQuantity
                this.productPrice.value = currentProductPrice
            }
        }
    }

    fun productDelete(receiptIndex: Int, itemIndex: Int): Boolean {
        val currentList = _receipts.value.toMutableList()
        return if (receiptIndex in currentList.indices) {
            val receipt = currentList[receiptIndex]
            val currentProductName = receipt.productName.value.toMutableList()
            val currentProductQuantity = receipt.productQuantity.value.toMutableList()
            val currentProductPrice = receipt.productPrice.value.toMutableList()
            currentProductName.removeAt(itemIndex)
            currentProductQuantity.removeAt(itemIndex)
            currentProductPrice.removeAt(itemIndex)
            receipt.productName.value = currentProductName
            receipt.productQuantity.value = currentProductQuantity
            receipt.productPrice.value = currentProductPrice
            _receipts.value = currentList
            true
        } else false
    }

    fun validateAndCleanReceipts(): Boolean {
        val currentList = _receipts.value.toMutableList()
        val hasValidReceipt = currentList.any { receipt ->
            receipt.productName.value.isNotEmpty()
        }
        if (hasValidReceipt) {
            val emptyReceiptIndices = currentList.indices
                .filter { index -> currentList[index].productName.value.isEmpty() }
                .sortedDescending()
            emptyReceiptIndices.forEach { index ->
                receiptDelete(index)
            }
        }
        return hasValidReceipt
    }

    companion object {
        private val DEFAULT_RECEIPT = ReceiptClass(
            placeName = "영수증",
            productName = MutableStateFlow(mutableListOf()),
            productPrice = MutableStateFlow(mutableListOf()),
            productQuantity = MutableStateFlow(mutableListOf())
        )
    }
}