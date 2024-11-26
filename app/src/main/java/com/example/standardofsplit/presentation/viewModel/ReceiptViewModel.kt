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

    fun addReceipt(receipt: ReceiptClass) {
        receiptUseCase.addReceipt(receipt)
    }

    fun updateReceiptName(index: Int, newName: String) {
        receiptUseCase.updateReceiptName(index, newName)
    }

    fun updateReceiptDetail(
        index: Int,
        itemIndex: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        receiptUseCase.updateReceiptDetail(index, itemIndex, productName, productQuantity, productPrice)
    }

    fun addReceiptItem(
        index: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        receiptUseCase.addReceiptItem(index, productName, productQuantity, productPrice)
    }

    fun deleteReceipt(index: Int) {
        receiptUseCase.deleteReceipt(index)
    }

    fun deleteReceiptItem(receiptIndex: Int, itemIndex: Int) {
        receiptUseCase.deleteReceiptItem(receiptIndex, itemIndex)
    }

    fun validateReceipts(): Boolean {
        return receiptUseCase.validateAndCleanReceipts()
    }
}