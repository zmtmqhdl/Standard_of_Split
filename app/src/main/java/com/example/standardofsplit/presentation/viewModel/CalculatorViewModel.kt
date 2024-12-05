package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.TotalPay
import com.example.standardofsplit.domain.usecase.CalculatorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculatorUseCase: CalculatorUseCase,
    private val startViewModel: StartViewModel
): ViewModel() {

    private val personCount: StateFlow<Int> = startViewModel.personCount

    private val _totalPay = MutableStateFlow(TotalPay())
    val totalPay: StateFlow<TotalPay> = _totalPay

    private val _stack = MutableStateFlow<MutableList<Any>>(mutableListOf())
    val stack: StateFlow<MutableList<Any>> = _stack

    private val _buttonNames = MutableStateFlow<Map<Int, String>>(emptyMap())
    val buttonNames: StateFlow<Map<Int, String>> = _buttonNames

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
        calculatorUseCase.setReceiptKey(value = 0)
        calculatorUseCase.setProductKey(value = 0)
    }

    fun updateTotalPay(payList: List<Int>, productPrice: Int, placeName: String, productName: String) {
        calculatorUseCase.updateTotalPay(totalPay.value, payList, productPrice, placeName, productName)
    }

    fun setReceiptKey(value: Int) {
        calculatorUseCase.setProductKey(value)
    }

    fun incrementReceiptKey() {
        calculatorUseCase.incrementReceiptKey()
    }

    fun decrementReceiptKey() {
        calculatorUseCase.decrementReceiptKey()
    }

    fun setProductKey(value: Int) {
        calculatorUseCase.setProductKey(value)
    }

    fun incrementProductKey() {
        calculatorUseCase.incrementProductKey()
    }

    fun decrementProductKey() {
        calculatorUseCase.decrementProductKey()
    }

    fun initializeButtonNames() {
        calculatorUseCase.initializeButtonNames(personCount.value)
    }

    fun updateButtonNames(index: Int, newName: String) {
        calculatorUseCase.updateButtonNames(index, newName, _buttonNames.value)
    }

//
//    val _showToastEvent = MutableLiveData<Boolean>()
//    val showToastEvent: LiveData<Boolean> = _showToastEvent
//
//    fun reDo() {
//        val currentStack = _stack.value ?: mutableListOf()
//        if (currentStack.isNotEmpty()) {
//            val currentKey = _receiptKey.value ?: 0
//            val currentKeyKey = _productKey.value ?: 0
//
//            if (currentStack.isEmpty()) {
//                _showToastEvent.value = true
//                return
//            }
//
//            try {
//                val lastElement = currentStack.removeAt(currentStack.size - 1)
//                _totalPay.value = lastElement as? MutableMap<Int, MutableMap<String, MutableMap<String, Int>>>
//                _stack.value = currentStack
//
//                if (currentKeyKey == 0) {
//                    if (currentKey > 0) {
//                        val prevSize = _previousReceiptSize.value ?: 0
//                        decrementReceiptKey()
//                        _productKey.value = prevSize - 1
//                    } else {
//                        _productKey.value = 0
//                        _showToastEvent.value = true
//                    }
//                } else {
//                    decrementProductKey()
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _showToastEvent.value = true
//            }
//        } else {
//            _showToastEvent.value = true
//        }
//    }
//

//
//    private val _isResetFromResult = MutableLiveData<Boolean>(false)
//    val isResetFromResult: LiveData<Boolean> = _isResetFromResult
//
//    fun setResetFromResult(value: Boolean) {
//        _isResetFromResult.value = value
//    }
//
}