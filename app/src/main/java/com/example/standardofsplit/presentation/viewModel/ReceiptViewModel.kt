package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.ReceiptClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor() : ViewModel() {

    private val _receipts = MutableStateFlow<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: StateFlow<MutableList<ReceiptClass>> = _receipts

    init {
        _receipts.value.add(DEFAULT_RECEIPT)
    }

    fun receiptAdd(receipt: ReceiptClass) {
        _receipts.value.add(receipt)
    }

    fun receiptUpdate(index: Int, newName: String) {
        _receipts.value[index].placeName = newName
    }

    fun receiptDelete(index: Int): Boolean {
        return if (index in _receipts.value.indices) {
            _receipts.value.removeAt(index)
            true
        } else false
    }

    fun productAdd(index: Int, productName: String, productQuantity: String, productPrice: String) {
        _receipts.value[index].apply {
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

    fun productUpdate(index: Int, productIndex: Int, productName: String, productQuantity: String, productPrice: String) {
        _receipts.value[index].apply {
            val currentProductName = this.productName.value.toMutableList()
            val currentProductQuantity = this.productQuantity.value.toMutableList()
            val currentProductPrice = this.productPrice.value.toMutableList()
            currentProductName[productIndex] = productName
            currentProductQuantity[productIndex] = productQuantity
            currentProductPrice[productIndex] = productPrice
            this.productName.value = currentProductName
            this.productQuantity.value = currentProductQuantity
            this.productPrice.value = currentProductPrice
        }
    }

    fun deleteReceiptItem(receiptIndex: Int, itemIndex: Int): Boolean {
        return if (receiptIndex in _receipts.value.indices) {
            val receipt = _receipts.value[receiptIndex]
            val currentProductName = receipt.productName.value.toMutableList()
            val currentProductQuantity = receipt.productQuantity.value.toMutableList()
            val currentProductPrice = receipt.productPrice.value.toMutableList()
            currentProductName.removeAt(itemIndex)
            currentProductQuantity.removeAt(itemIndex)
            currentProductPrice.removeAt(itemIndex)
            receipt.productName.value = currentProductName
            receipt.productQuantity.value = currentProductQuantity
            receipt.productPrice.value = currentProductPrice
            true
        } else false
    }

    fun validateReceipts(): Boolean {
        val hasValidReceipt = _receipts.value.any { receipt ->
            receipt.productName.value.isNotEmpty()
        }
        if (hasValidReceipt) {
            val emptyReceiptIndices = _receipts.value.indices
                .filter { index -> _receipts.value[index].productName.value.isEmpty() }
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