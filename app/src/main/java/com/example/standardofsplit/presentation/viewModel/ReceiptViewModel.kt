package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.ReceiptClass
import com.example.standardofsplit.domain.useCase.ReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val receiptUseCase: ReceiptUseCase
) : ViewModel() {

    val receipts: StateFlow<MutableList<ReceiptClass>> = receiptUseCase.receipts

    fun receiptAdd(receipt: ReceiptClass) {
        receiptUseCase.receiptAdd(receipt)
    }

    fun receiptUpdate(index: Int, newName: String) {
        receiptUseCase.receiptUpdate(index, newName)
    }

    fun receiptDelete(index: Int) {
        receiptUseCase.receiptDelete(index)
    }

    fun productAdd(
        index: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        receiptUseCase.productAdd(index, productName, productQuantity, productPrice)
    }

    fun productUpdate(
        index: Int,
        productIndex: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        receiptUseCase.productUpdate(index, productIndex, productName, productQuantity, productPrice)
    }

    fun deleteReceiptItem(receiptIndex: Int, itemIndex: Int) {
        receiptUseCase.productDelete(receiptIndex, itemIndex)
    }

    fun validateReceipts(): Boolean {
        return receiptUseCase.validateAndCleanReceipts()
    }
}