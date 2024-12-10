package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.ReceiptClass
import com.example.standardofsplit.data.model.TotalPay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    startViewModel: StartViewModel,
    receiptViewModel: ReceiptViewModel
): ViewModel() {

    private val personCount: StateFlow<Int> = startViewModel.personCount

    private val receipts: StateFlow<List<ReceiptClass>> = receiptViewModel.receipts

    private val _totalPay = MutableStateFlow(TotalPay())

    private val _stack = MutableStateFlow<MutableList<Any>>(mutableListOf())

    private val _buttonNames = MutableStateFlow<MutableList<String>>(mutableListOf())
    val buttonNames: StateFlow<MutableList<String>> = _buttonNames

    private val _buttonState = MutableStateFlow(List(8) { false })
    val buttonState: StateFlow<List<Boolean>> = _buttonState

    private val _buttonPermissions = MutableStateFlow(List(8) { false })
    val buttonPermissions: StateFlow<List<Boolean>> = _buttonPermissions

    private val _receiptKey = MutableStateFlow(0)
    val receiptKey: StateFlow<Int> = _receiptKey

    private val _productKey = MutableStateFlow(0)
    val productKey: StateFlow<Int> = _productKey

    private val _changeMode = MutableStateFlow(false)

    private val _buttonNameChangeDialog = MutableStateFlow(false)

    fun setChangeMode() {
        _changeMode.value = !_changeMode.value
    }

    fun buttonPush(index: Int) {
        _buttonState.value = _buttonState.value.toMutableList().apply { this[index] = !this[index] }
    }

    fun resetButtonStates() {
        _buttonState.value = List(8) { false }
    }

    fun initializeTotalPay() {
        val initialTotalPay = (0..7).associateWith { mutableMapOf<String, MutableMap<String, Int>>() }
        _totalPay.value.payment.value = initialTotalPay.toMutableMap()
        setReceiptKey(value = 0)
        setProductKey(value = 0)
    }

    fun updateTotalPay(payList: List<Int>, productPrice: Int, placeName: String, productName: String) {
        try {
            val dividedPrice: Int = (kotlin.math.ceil((productPrice.toDouble() / payList.size) / 10) * 10).toInt()
            val current = _totalPay.value.payment.value

            for (i in payList) {
                val updatedProducts = current[i]?.get(placeName) ?: mutableMapOf()
                updatedProducts[productName] = dividedPrice
                current[i] = current[i]?.apply { this[placeName] = updatedProducts } ?: mutableMapOf(placeName to updatedProducts)
            }
            _totalPay.value.payment.value = current
        } catch (_: Exception) {
        }
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
        _buttonNames.value.clear()
        for (i in 0..7) {
            if (i < personCount.value) {
                _buttonNames.value.add("X")
                _buttonPermissions.value = _buttonPermissions.value.toMutableList().apply { this[i] = true }
            } else {
                _buttonNames.value.add("인원$i")
            }
        }
    }

    fun updateButtonNames(index: Int, newName: String) {
        _buttonNames.value = _buttonNames.value.toMutableList().apply { this[index] = newName }
    }

    fun rollback() {
        val currentStack = _stack.value
        if (currentStack.isNotEmpty()) {
            val lastElement = currentStack.removeAt(currentStack.size - 1)
            _totalPay.value = lastElement as TotalPay
            _stack.value = currentStack
            if (_productKey.value == 0) {
                if (_receiptKey.value > 0) {
                    decrementReceiptKey()
                    setProductKey(lastElement.payment.value.size - 1)
                }
            } else {
                decrementProductKey()
            }
        }
    }

    private fun lastCheck(): Boolean {
        val receiptCount = receipts.value.size
        val productCount = receipts.value[receiptCount - 1].productName.value.size
        return (receiptCount - 1 == _receiptKey.value && productCount - 1 == _productKey.value)
    }

    fun personSelect(index: Int) {
        if (_changeMode.value && _buttonPermissions.value[index]) {
            _buttonNameChangeDialog.value = true
        } else if (!_changeMode.value && lastCheck()) {
            // showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
        } else if (!_changeMode.value && !lastCheck()) {
            buttonPush(index)
        }
    }
}