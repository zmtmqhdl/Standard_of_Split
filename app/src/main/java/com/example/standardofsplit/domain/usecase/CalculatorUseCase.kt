package com.example.standardofsplit.domain.usecase

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CalculatorUseCase @Inject constructor() {

    private val _buttonState = MutableStateFlow(List(9) { false })
    val buttonState = _buttonState

    private val _receiptKey = MutableStateFlow(0)
    val receiptKey = _receiptKey

    private val _productKey = MutableStateFlow(0)
    val productKey = _productKey

    fun buttonPush(index: Int) {
        _buttonState.value = _buttonState.value.mapIndexed { i, value ->
            if (i == index) !value else value
        }
    }

    fun incrementReceiptKey() {
        _receiptKey.value += 1 // Receipt Key 증가
    }

    fun decrementReceiptKey() {
        _receiptKey.value -= 1 // Receipt Key 감소
    }

    fun incrementProductKey() {
        _productKey.value += 1 // Product Key 증가
    }

    fun decrementProductKey() {
        _productKey.value -= 1 // Product Key 감소
    }

    fun resetKeyKey() {
        _productKey.value = 0 // Product Key 초기화
    }
}