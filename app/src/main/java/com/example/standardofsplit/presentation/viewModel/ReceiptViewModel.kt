package com.example.standardofsplit.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.ReceiptClass
import com.example.standardofsplit.presentation.ui.component.showCustomToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor() : ViewModel() {

    private val _receipts = MutableStateFlow<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: StateFlow<MutableList<ReceiptClass>> = _receipts

    private val _showReceiptAddDialog = MutableStateFlow(false)
    val showReceiptAddDialog: StateFlow<Boolean> = _showReceiptAddDialog

    private val _showReceiptNameUpdateDialog = MutableStateFlow<Int?>(null)
    val showReceiptNameUpdateDialog: StateFlow<Int?> = _showReceiptNameUpdateDialog

    private val _showProductAddDialog = MutableStateFlow<Int?>(null)
    val showProductAddDialog: StateFlow<Int?> = _showProductAddDialog

    private val _showProductUpdateDialog = MutableStateFlow<Pair<Int, Int>?>(null)
    val showProductUpdateDialog: StateFlow<Pair<Int, Int>?> = _showProductUpdateDialog

    init {
        _receipts.value.add(DEFAULT_RECEIPT)
    }

    fun changeProductUpdateDialog(mode: Pair<Int, Int>?) {
        if (mode == null) {
            _showProductUpdateDialog.value = null
        } else {
            _showProductUpdateDialog.value = mode
        }
    }

    fun changeReceiptAddDialog() {
        _showReceiptAddDialog.value = !showReceiptAddDialog.value
    }

    fun changeProductAddDialog(mode: Int?) {
        if (mode == null) {
            _showProductAddDialog.value = null
        } else {
            _showProductAddDialog.value = mode
        }
    }

    fun changeReceiptNameUpdateDialog(mode: Int?) {
        if (mode == null) {
            _showReceiptNameUpdateDialog.value = null
        } else {
            _showReceiptNameUpdateDialog.value = mode
        }
    }


    fun receiptAdd(receipt: ReceiptClass) {
        val currentReceipts = _receipts.value.toMutableList()
        currentReceipts.add(receipt)
        _receipts.value = currentReceipts
    }

    fun receiptUpdate(index: Int, newName: String) {
        val currentReceipts = _receipts.value.toMutableList()
        currentReceipts[index] = currentReceipts[index].copy(placeName = newName)
        _receipts.value = currentReceipts
    }

    fun receiptDelete(index: Int) {
        val currentReceipts = _receipts.value.toMutableList()
        currentReceipts.removeAt(index)
        _receipts.value = currentReceipts
    }

    fun productAdd(index: Int, productName: String, productQuantity: Int, productPrice: Int) {
        _receipts.value[index].productName.value = _receipts.value[index].productName.value.toMutableList().apply { add(productName) }
        _receipts.value[index].productQuantity.value = _receipts.value[index].productQuantity.value.toMutableList().apply { add(productQuantity) }
        _receipts.value[index].productPrice.value = _receipts.value[index].productPrice.value.toMutableList().apply { add(productPrice) }
    }

    fun productUpdate(index: Int, productIndex: Int, productName: String, productQuantity: Int, productPrice: Int) {
        val currentProductNames = _receipts.value[index].productName.value.toMutableList()
        val currentProductQuantities = _receipts.value[index].productQuantity.value.toMutableList()
        val currentProductPrices = _receipts.value[index].productPrice.value.toMutableList()
        currentProductNames[productIndex] = productName
        currentProductQuantities[productIndex] = productQuantity
        currentProductPrices[productIndex] = productPrice
        _receipts.value[index].productName.value = currentProductNames
        _receipts.value[index].productQuantity.value = currentProductQuantities
        _receipts.value[index].productPrice.value = currentProductPrices
    }

    fun deleteReceiptItem(receiptIndex: Int, itemIndex: Int) {
        if (receiptIndex in _receipts.value.indices) {
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
        }
    }

    private fun validateReceipts(): Boolean {
        val hasValidReceipt = _receipts.value.any { receipt ->
            receipt.productName.value.isNotEmpty()
        }
        if (hasValidReceipt) {
            val currentReceipts = _receipts.value.filter { receipt ->
                receipt.productName.value.isNotEmpty()
            }.toMutableList()
            _receipts.value = currentReceipts
        }
        return hasValidReceipt
    }

    fun receiptCheckAndNext (
        onNext: () -> Unit,
        context: Context
    ) {
        if (validateReceipts()) {
            onNext()
        } else {
            showCustomToast(message = "최소 1개 이상의 상품이 포함된 영수증이 1개 이상 필요합니다.", context = context)
        }
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