package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.TotalPay
import com.example.standardofsplit.domain.usecase.CalculatorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculatorUseCase: CalculatorUseCase,
    private val startViewModel: StartViewModel
): ViewModel() {

    val changeMode: StateFlow<Boolean> = calculatorUseCase.changeMode

    private val personCount: StateFlow<Int> = startViewModel.personCount

    private val _totalPay = MutableStateFlow(TotalPay())
    val totalPay: StateFlow<TotalPay> = _totalPay

    private val _stack = MutableStateFlow<MutableList<Any>>(mutableListOf())
    val stack: StateFlow<MutableList<Any>> = _stack

    private val _buttonNames = MutableStateFlow<Map<Int, String>>(emptyMap())
    val buttonNames: StateFlow<Map<Int, String>> = _buttonNames

    private val _receiptKey = MutableStateFlow(0)
    val receiptKey: StateFlow<Int> = _receiptKey

    private val _productKey = MutableStateFlow(0)
    val productKey: StateFlow<Int> = _productKey

    fun setChangeMode(value: Boolean) {
        calculatorUseCase.setChangeMode(value)
    }

    fun buttonPush(index: Int) {
        calculatorUseCase.buttonPush(index)
    }

    fun resetButtonStates() {
        calculatorUseCase.resetButtonStates()
    }

    fun initializeTotalPay() {
        calculatorUseCase.initializeTotalPay(_totalPay.value)
        setReceiptKey(value = 0)
        setProductKey(value = 0)
    }

    fun updateTotalPay(payList: List<Int>, productPrice: Int, placeName: String, productName: String) {
        calculatorUseCase.updateTotalPay(totalPay.value, payList, productPrice, placeName, productName)
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

    fun incrementProductKey() {
        _productKey.value += 1
    }

    fun decrementProductKey() {
        _productKey.value -= 1
    }

    fun initializeButtonNames() {
        calculatorUseCase.initializeButtonNames(personCount.value)
    }

    fun updateButtonNames(index: Int, newName: String) {
        calculatorUseCase.updateButtonNames(index, newName, _buttonNames.value)
    }

    fun rollback() {
        calculatorUseCase.rollback(_stack, _totalPay)
    }
}