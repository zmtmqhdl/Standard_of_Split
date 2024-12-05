package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.ReceiptClass
import com.example.standardofsplit.domain.usecase.ReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val receiptUseCase: ReceiptUseCase
) : ViewModel() {

    private val _receipts = MutableStateFlow<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: StateFlow<MutableList<ReceiptClass>> = _receipts

    init {
        _receipts.value.add(DEFAULT_RECEIPT)
    }

    fun receiptAdd(receipt: ReceiptClass) {
        receiptUseCase.receiptAdd(_receipts.value, receipt)
    }

    fun receiptUpdate(index: Int, newName: String) {
        receiptUseCase.receiptUpdate(_receipts.value, index, newName)
    }

    fun receiptDelete(index: Int) {
        receiptUseCase.receiptDelete(_receipts.value, index)
    }

    fun productAdd(index: Int, productName: String, productQuantity: String, productPrice: String
    ) {
        receiptUseCase.productAdd(_receipts.value, index, productName, productQuantity, productPrice)
    }

    fun productUpdate(index: Int, productIndex: Int, productName: String, productQuantity: String, productPrice: String
    ) {
        receiptUseCase.productUpdate(_receipts.value, index, productIndex, productName, productQuantity, productPrice)
    }

    fun deleteReceiptItem(receiptIndex: Int, itemIndex: Int) {
        receiptUseCase.productDelete(_receipts.value, receiptIndex, itemIndex)
    }

    fun validateReceipts(): Boolean {
        return receiptUseCase.validateAndCleanReceipts(_receipts.value)
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