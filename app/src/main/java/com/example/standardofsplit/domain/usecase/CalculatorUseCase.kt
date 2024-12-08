package com.example.standardofsplit.domain.usecase

import com.example.standardofsplit.data.model.TotalPay
import com.example.standardofsplit.presentation.viewModel.CalculatorViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Thread.State
import javax.inject.Inject
import kotlin.math.ceil

class CalculatorUseCase @Inject constructor() {

    private val _changeMode = MutableStateFlow(false)
    val changeMode: StateFlow<Boolean> = _changeMode.asStateFlow()

    private val _buttonState = MutableStateFlow(List(8) { false })
    val buttonState: StateFlow<List<Boolean>> = _buttonState

    fun setChangeMode(value: Boolean) {
        _changeMode.value = value
    }

    fun buttonPush(index: Int) {
        _buttonState.value = _buttonState.value.mapIndexed { i, value ->
            if (i == index) !value else value
        }
    }

    fun initializeTotalPay(currentTotalPay: TotalPay) {
        val initialTotalPay =
            (0..7).associateWith { mutableMapOf<String, MutableMap<String, Int>>() }
        currentTotalPay.payment.value = initialTotalPay.toMutableMap()
    }

    fun updateTotalPay(
        currentTotalPay: TotalPay,
        payList: List<Int>,
        productPrice: Int,
        placeName: String,
        productName: String
    ) {
        try {
            val dividedPrice: Int =
                (ceil((productPrice.toDouble() / payList.size) / 10) * 10).toInt()
            val current = currentTotalPay.payment.value

            for (i in payList) {
                val updatedProducts = current[i]?.get(placeName) ?: mutableMapOf()
                updatedProducts[productName] = dividedPrice
                current[i] =
                    current[i]?.apply { this[placeName] = updatedProducts } ?: mutableMapOf(
                        placeName to updatedProducts
                    )
            }
            currentTotalPay.payment.value = current
        } catch (e: Exception) {
            // 토스트메시지
        }
    }

    fun resetButtonStates() {
        _buttonState.value = List(9) { false }
    }

    fun initializeButtonNames(personCount: Int) {
        val currentNames = mutableMapOf<Int, String>()
        for (i in 0..7) {
            if (i <= personCount) {
                currentNames[i] = "X"
            } else {
                currentNames[i] = "인원$i"
            }
        }
    }

    fun updateButtonNames(index: Int, newName: String, beforeNames: Map<Int, String>) {
        beforeNames.toMutableMap()[index] = newName
    }

    fun rollback(
        stack: MutableStateFlow<MutableList<Any>>,
        totalPay: MutableStateFlow<TotalPay>
    ) {
        val currentStack = stack.value
        if (currentStack.isNotEmpty()) {
            val lastElement = currentStack.removeAt(currentStack.size - 1)
            totalPay.value = lastElement as TotalPay
            stack.value = currentStack
            if (_productKey.value == 0) {
                if (_receiptKey.value > 0) {
                    decrementReceiptKey()
                    setProductKey(lastElement.payment.value.size - 1)
                } else {
                    // 되돌릴게 없어요
                }
            } else {
                decrementProductKey()
            }
        } else {
            // 되돌릴게 없어요 토스트메시지
        }
    }
}
