package com.example.standardofsplit.domain.usecase

import com.example.standardofsplit.data.model.TotalPay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class CalculatorUseCase @Inject constructor() {

    private val _changeMode = MutableStateFlow(false)
    val changeMode: StateFlow<Boolean> = _changeMode.asStateFlow()

    private val _buttonState = MutableStateFlow(List(9) { false })
    val buttonState: StateFlow<List<Boolean>> = _buttonState

    private val _receiptKey = MutableStateFlow(0)
    val receiptKey: StateFlow<Int> = _receiptKey

    private val _productKey = MutableStateFlow(0)
    val productKey: StateFlow<Int> = _productKey

    private val _buttonPermission = MutableStateFlow(
        (1..8).associate { it.toString() to false }
    )
    val buttonPermissions: StateFlow<Map<String, Boolean>> = _buttonPermission

    fun setChangeMode(value: Boolean) {
        _changeMode.value = value
    }

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

    fun setReceiptKey(value: Int) {
        _receiptKey.value = value
    }

    fun incrementReceiptKey() {
        _receiptKey.value += 1
    }

    fun decrementReceiptKey() {
        _receiptKey.value -= 1
    }

    fun setProductKey(value: Int) {
        _productKey.value = value
    }

    fun resetProductKey() {
        _productKey.value = 0
    }

    fun incrementProductKey() {
        _productKey.value += 1
    }

    fun decrementProductKey() {
        _productKey.value -= 1
    }

    fun initializeButtonNames(currentPermissions: Map<String, Boolean>): Map<String, String> {
        val currentNames = mutableMapOf<String, String>()
        for (i in 1..8) {
            val key = i.toString()
            if (currentPermissions[key] == false) {
                currentNames[key] = "X"
            } else {
                currentNames[key] = "인원$i"
            }
        }
        return currentNames
    }

    fun updateButtonNames(index: String, newName: String, beforeNames: Map<String, String>): Map<String, String> {
        val currentNames = beforeNames.toMutableMap()
        currentNames[index] = newName
        return currentNames
    }

}