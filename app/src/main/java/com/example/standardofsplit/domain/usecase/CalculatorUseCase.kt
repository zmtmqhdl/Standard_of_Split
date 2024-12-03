package com.example.standardofsplit.domain.usecase

import com.example.standardofsplit.data.model.TotalPay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CalculatorUseCase @Inject constructor() {

    private val _buttonState = MutableStateFlow(List(9) { false })
    val buttonState: StateFlow<List<Boolean>> = _buttonState

    private val _receiptKey = MutableStateFlow(0)
    val receiptKey: StateFlow<Int> = _receiptKey

    private val _productKey = MutableStateFlow(0)
    val productKey: StateFlow<Int> = _productKey

    fun buttonPush(index: Int) {
        _buttonState.value = _buttonState.value.mapIndexed { i, value ->
            if (i == index) !value else value
        }
    }

    fun updateTotalPay(currentTotalPay: TotalPay): TotalPay {
        val updatedPayments = currentTotalPay.payments.mapValues { entry ->
            entry.value.toMutableMap()
        }.toMutableMap()
        return TotalPay(updatedPayments)
    }

    fun resetButtonStates() {
        _buttonState.value = List(9) { false }
    }

    fun incrementReceiptKey() {
        _receiptKey.value += 1
    }

    fun decrementReceiptKey() {
        _receiptKey.value -= 1
    }

    fun incrementProductKey() {
        _productKey.value += 1
    }

    fun decrementProductKey() {
        _productKey.value -= 1
    }

    fun resetProductKey() {
        _productKey.value = 0
    }


}