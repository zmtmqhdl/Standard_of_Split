package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.ReceiptClass
import com.example.standardofsplit.domain.useCase.ReceiptUseCase
import com.example.standardofsplit.presentation.event.ReceiptEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val receiptUseCase: ReceiptUseCase
) : ViewModel() {

    val receipts: StateFlow<MutableList<ReceiptClass>> = receiptUseCase.receipts

    fun onEvent(event: ReceiptEvent) {
        when (event) {
            is ReceiptEvent.AddReceipt -> {
                receiptUseCase.addReceipt(event.receipt)
            }
            is ReceiptEvent.UpdateReceiptName -> {
                receiptUseCase.updateReceiptName(event.index, event.newName)
            }
            is ReceiptEvent.UpdateReceiptDetail -> {
                receiptUseCase.updateReceiptDetail(
                    event.index,
                    event.itemIndex,
                    event.productName,
                    event.productQuantity,
                    event.productPrice
                )
            }
            is ReceiptEvent.AddReceiptItem -> {
                receiptUseCase.addReceiptItem(
                    event.index,
                    event.productName,
                    event.productQuantity,
                    event.productPrice
                )
            }
            is ReceiptEvent.DeleteReceipt -> {
                receiptUseCase.deleteReceipt(event.index)
            }
            is ReceiptEvent.DeleteReceiptItem -> {
                receiptUseCase.deleteReceiptItem(event.receiptIndex, event.itemIndex)
            }
            is ReceiptEvent.CheckReceipts -> {
                receiptUseCase.validateAndCleanReceipts()
            }
        }
    }
}