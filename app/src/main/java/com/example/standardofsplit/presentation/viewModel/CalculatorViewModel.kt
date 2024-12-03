package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.TotalPay
import com.example.standardofsplit.domain.usecase.CalculatorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculatorUseCase: CalculatorUseCase
): ViewModel() {

    private val _changeMode = MutableStateFlow(false)
    val changeMode: StateFlow<Boolean> = _changeMode.asStateFlow()

    private val _totalPay = MutableStateFlow(TotalPay())
    val totalPay: StateFlow<TotalPay> = _totalPay

    fun buttonPush(index: Int) {
        calculatorUseCase.buttonPush(index)
    }

    fun resetButtonStates() {
        calculatorUseCase.resetButtonStates()
    }

    fun initializeTotalPay() {
        val initialTotalPay = (1..8).associateWith { mutableMapOf<String, MutableMap<String, Int>>() }
        _totalPay.value = TotalPay(initialTotalPay.toMutableMap())
    }

    fun updateTotalPay() {
        _totalPay.value = calculatorUseCase.updateTotalPay(totalPay.value)
    }

    fun incrementReceiptKey() {
        calculatorUseCase.incrementReceiptKey()
    }

    fun decrementReceiptKey() {
        calculatorUseCase.decrementReceiptKey()
    }

    fun incrementProductKey() {
        calculatorUseCase.incrementProductKey()
    }

    fun decrementProductKey() {
        calculatorUseCase.decrementProductKey()
    }

    fun resetProductKey() {
        calculatorUseCase.resetProductKey()
    }



    fun updateTotalPay(lst: List<Int>, placeName: String, productName: String, productPrice: Int) {
        try {
            val dividedPrice = (ceil((productPrice.toDouble() / lst.size) / 10) * 10).toInt()

            val updatedPersonPay = _totalPay.value?.toMutableMap() ?: return

            for (i in lst) {
                val current = updatedPersonPay[i] ?: mutableMapOf()
                val updatedProducts = current[placeName]?.toMutableMap() ?: mutableMapOf()
                updatedProducts[productName] = dividedPrice
                current[placeName] = updatedProducts
                updatedPersonPay[i] = current
            }

            _totalPay.value = updatedPersonPay

            _stack.value = (_stack.value ?: mutableListOf()).apply {
                add(updatedPersonPay.toMutableMap())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _showToastEvent.value = true
        }
    }

    private val _stack = MutableLiveData<MutableList<Any>>(mutableListOf())
    val stack: LiveData<MutableList<Any>> = _stack

    val _showToastEvent = MutableLiveData<Boolean>()
    val showToastEvent: LiveData<Boolean> = _showToastEvent

    private val _currentReceiptSize = MutableLiveData<Int>(0)
    val currentReceiptSize: LiveData<Int> = _currentReceiptSize

    fun updateCurrentReceiptSize(size: Int) {
        _currentReceiptSize.value = size
    }

    private val _previousReceiptSize = MutableLiveData<Int>(0)
    val previousReceiptSize: LiveData<Int> = _previousReceiptSize

    fun updatePreviousReceiptSize(size: Int) {
        _previousReceiptSize.value = size
    }

    fun setKey(value: Int) {
        _receiptKey.value = value
    }

    fun setKeyKey(value: Int) {
        _productKey.value = value
    }

    fun reDo() {
        val currentStack = _stack.value ?: mutableListOf()
        if (currentStack.isNotEmpty()) {
            val currentKey = _receiptKey.value ?: 0
            val currentKeyKey = _productKey.value ?: 0

            if (currentStack.isEmpty()) {
                _showToastEvent.value = true
                return
            }

            try {
                val lastElement = currentStack.removeAt(currentStack.size - 1)
                _totalPay.value = lastElement as? MutableMap<Int, MutableMap<String, MutableMap<String, Int>>>
                _stack.value = currentStack
                
                if (currentKeyKey == 0) {
                    if (currentKey > 0) {
                        val prevSize = _previousReceiptSize.value ?: 0
                        decrementReceiptKey()
                        _productKey.value = prevSize - 1
                    } else {
                        _productKey.value = 0
                        _showToastEvent.value = true
                    }
                } else {
                    decrementProductKey()
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
                _showToastEvent.value = true
            }
        } else {
            _showToastEvent.value = true
        }
    }

    private val _buttonNames = MutableLiveData<Map<String, String>>(
        (1..8).associate { it.toString() to "인원$it" }
    )
    val buttonNames: LiveData<Map<String, String>> = _buttonNames

    fun updateButtonName(index: String, newName: String) {
        _buttonNames.value = _buttonNames.value?.toMutableMap()?.apply {
            this[index] = newName
        }
    }

    private val _buttonPermissions = MutableLiveData<Map<String, Boolean>>(
        (1..8).associate { it.toString() to false }
    )
    val buttonPermissions: LiveData<Map<String, Boolean>> = _buttonPermissions

    fun updateButtonPermissions(personCount: Int) {
        _buttonPermissions.value = (1..8).associate { i ->
            i.toString() to (i <= personCount)
        }
    }

    private val _isResetFromResult = MutableLiveData<Boolean>(false)
    val isResetFromResult: LiveData<Boolean> = _isResetFromResult

    fun setResetFromResult(value: Boolean) {
        _isResetFromResult.value = value
    }

    fun resetPersonPay() {
        val newMap = (0..7).associate { index ->  // 0부터 7까지로 변경
            index to mutableMapOf<String, MutableMap<String, Int>>()
        }.toMutableMap()

        _totalPay.value = newMap
        _stack.value = mutableListOf()
        _receiptKey.value = 0
        _productKey.value = 0
    }

    fun updateButtonNamesBasedOnPermissions() {
        val currentPermissions = _buttonPermissions.value ?: return
        val currentNames = _buttonNames.value?.toMutableMap() ?: return
        
        for (i in 1..8) {
            val key = i.toString()
            if (currentPermissions[key] == false) {
                currentNames[key] = "X"
            } else {
                if (currentNames[key] == "X") {
                    currentNames[key] = "인원$i"
                }
            }
        }
        _buttonNames.value = currentNames
    }

    fun setChangeMode(value: Boolean) {
        _changeMode.value = value
    }
}