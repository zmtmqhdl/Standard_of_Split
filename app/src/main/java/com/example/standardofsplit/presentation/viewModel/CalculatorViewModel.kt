package com.example.standardofsplit.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.data.model.ReceiptClass
import com.example.standardofsplit.data.model.TotalPay
import com.example.standardofsplit.presentation.ui.component.showCustomToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor() : ViewModel() {

    private val _totalPay = MutableStateFlow(TotalPay())
    val totalPay: StateFlow<TotalPay> = _totalPay

    private val _stack = MutableStateFlow<MutableList<Any>>(mutableListOf())

    private val _buttonNames = MutableStateFlow(MutableList(8) { "X" })
    val buttonNames: StateFlow<MutableList<String>> = _buttonNames

    private val _buttonStates = MutableStateFlow(MutableList(8) { false })
    val buttonStates: StateFlow<MutableList<Boolean>> = _buttonStates

    private val _buttonPermissions = MutableStateFlow(List(8) { false })

    private val _receiptKey = MutableStateFlow(0)
    val receiptKey: StateFlow<Int> = _receiptKey

    private val _productKey = MutableStateFlow(0)
    val productKey: StateFlow<Int> = _productKey

    private val _changeMode = MutableStateFlow(false)
    val changeMode: StateFlow<Boolean> = _changeMode

    private val _showButtonNameChangeDialog = MutableStateFlow(false)
    val showButtonNameChangeDialog: StateFlow<Boolean> = _showButtonNameChangeDialog

    private val _index = MutableStateFlow(0)
    val index: StateFlow<Int> = _index

    private val _last = MutableStateFlow(false)
    val last: StateFlow<Boolean> = _last

    fun setChangeMode() {
        _changeMode.value = !_changeMode.value
    }

    private fun buttonPush(index: Int) {
        _buttonStates.value =
            _buttonStates.value.toMutableList().apply { this[index] = !this[index] }
    }

    private fun resetButtonStates() {
        _buttonStates.value = MutableList(8) { false }
    }

    private fun trueButtonStates() {
        _buttonStates.value = _buttonPermissions.value.toMutableList()
    }

    fun initializeTotalPay() {
        setReceiptKey()
        setProductKey(value = 0)
        val initialTotalPay =
            (0..7).associateWith { mutableMapOf<String, MutableMap<String, Int>>() }
        _totalPay.value.payment.value = initialTotalPay.toMutableMap()
    }

    private fun updateTotalPay(
        payList: List<Int>,
        placeName: String,
        productName: String,
        productPrice: Int,
    ) {
        val dividedPrice: Int =
            (kotlin.math.ceil((productPrice.toDouble() / payList.size) / 10) * 10).toInt()
        Log.d("payList", payList.toString())
        Log.d("dividedPrice", dividedPrice.toString())
        val current = _totalPay.value.payment.value

        for (i in payList) {
            val updatedProducts = current[i]?.get(placeName) ?: mutableMapOf()
            updatedProducts[productName] = dividedPrice
            current[i] =
                current[i]?.apply { this[placeName] = updatedProducts } ?: mutableMapOf(
                    placeName to updatedProducts
                )
        }
        _totalPay.value.payment.value = current
        _stack.value.add(_totalPay.value)
    }

    fun setReceiptKey() {
        _receiptKey.value = 0
    }

    private fun incrementReceiptKey() {
        _receiptKey.value += 1
    }

    private fun decrementReceiptKey() {
        _receiptKey.value -= 1
    }

    fun setProductKey(value: Int) {
        _productKey.value = value
    }

    private fun incrementProductKey() {
        _productKey.value += 1
    }

    private fun decrementProductKey() {
        _productKey.value -= 1
    }

    fun closeButtonNameChangeDialog() {
        _showButtonNameChangeDialog.value = false
    }

    fun initializeButtonNames(
        personCount: Int
    ) {
        _buttonNames.value = List(_buttonNames.value.size) { index ->
            val tmp = index + 1
            if (index < personCount) "인원$tmp" else "X"
        }.toMutableList()

        _buttonPermissions.value = List(_buttonPermissions.value.size) { index ->
            index < personCount
        }
    }

    fun indexUpdate(index: Int) {
        _index.value = index
    }

    fun updateButtonNames(index: Int, newName: String) {
        _buttonNames.value = _buttonNames.value.toMutableList().apply { this[index] = newName }
        _showButtonNameChangeDialog.value = false
    }

    fun lastSet() {
        _last.value = false
    }

    fun rollback(context: Context) {
        _last.value = false
        val currentStack = _stack.value
        if (currentStack.isNotEmpty() && !_changeMode.value) {
            val lastElement = currentStack.removeAt(currentStack.size - 1)
            _totalPay.value = lastElement as TotalPay
            _stack.value = currentStack
            if (_productKey.value == 0) {
                if (_receiptKey.value > 0) {
                    decrementReceiptKey()
                    setProductKey(lastElement.payment.value.size - 1)
                } else {
                    showCustomToast(message = "첫 정산입니다.", context = context)
                }
            } else {
                decrementProductKey()
            }
        }
    }

    fun personSelect(index: Int, context: Context) {
        if (_changeMode.value && _buttonPermissions.value[index]) {
            _showButtonNameChangeDialog.value = true
        } else if (!_changeMode.value && _last.value) {
            showCustomToast(message = "정산이 완료되었습니다. 정산을 확인해주세요.", context = context)
        } else if (!_changeMode.value && !_last.value) {
            buttonPush(index)
        }
    }

    fun totalSelect(
        context: Context,
    ) {
        if (_last.value) {
            showCustomToast(message = "정산이 완료되었습니다. 정산을 확인해주세요.", context = context)
        } else {
            trueButtonStates()
        }
    }

    fun calculate(
        receipts: List<ReceiptClass>,
        onNext: () -> Unit,
        context: Context
    ) {
        if (_last.value) {
            onNext()
        } else {
            if (_buttonStates.value != List(8) { false }) {
                updateTotalPay(
                    payList = _buttonStates.value.mapIndexedNotNull { index, value ->
                        if (value) index else null
                    },
                    placeName = receipts[receiptKey.value].placeName,
                    productName = receipts[receiptKey.value].productName.value[productKey.value],
                    productPrice = receipts[receiptKey.value].productPrice.value[productKey.value],
                )
                resetButtonStates()
                if (receiptKey.value + 1 == receipts.size && productKey.value + 1 == receipts[receiptKey.value].productPrice.value.size) {
                    _last.value = true
                    showCustomToast(message = "정산이 완료되었습니다. 정산을 확인해주세요.", context = context)
                } else {
                    incrementProductKey()
                    if (productKey.value == receipts[receiptKey.value].productPrice.value.size && !_last.value) {
                        setProductKey(0)
                        incrementReceiptKey()
                    }
                }
            } else {
                showCustomToast(message = "정산할 인원을 선택해주세요.", context = context)
            }
        }
    }
}