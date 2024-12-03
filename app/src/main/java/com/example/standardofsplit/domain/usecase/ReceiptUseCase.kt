package com.example.standardofsplit.domain.usecase

import com.example.standardofsplit.data.model.ReceiptClass
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ReceiptUseCase @Inject constructor() {

    fun receiptAdd(receipts: MutableList<ReceiptClass>, receipt: ReceiptClass) {
        receipts.add(receipt)
    }

    fun receiptUpdate(receipts: MutableList<ReceiptClass>, index: Int, newName: String) {
        receipts[index].placeName = newName
    }

    fun receiptDelete(receipts: MutableList<ReceiptClass>, index: Int): Boolean {
        return if (index in receipts.indices) {
            receipts.removeAt(index)
            true
        } else false
    }

    fun productAdd(
        receipts: MutableList<ReceiptClass>,
        index: Int, productName: String, productQuantity: String, productPrice: String
    ) {
        receipts[index].apply {
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

    fun productUpdate(
        receipts: MutableList<ReceiptClass>,
        index: Int,
        itemIndex: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        receipts[index].apply {
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

    fun productDelete(receipts: MutableList<ReceiptClass>, receiptIndex: Int, itemIndex: Int): Boolean {
        return if (receiptIndex in receipts.indices) {
            val receipt = receipts[receiptIndex]
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

    fun validateAndCleanReceipts(receipts: MutableList<ReceiptClass>): Boolean {
        val hasValidReceipt = receipts.any { receipt ->
            receipt.productName.value.isNotEmpty()
        }
        if (hasValidReceipt) {
            val emptyReceiptIndices = receipts.indices
                .filter { index -> receipts[index].productName.value.isEmpty() }
                .sortedDescending()
            emptyReceiptIndices.forEach { index ->
                receiptDelete(receipts, index)
            }
        }
        return hasValidReceipt
    }
}