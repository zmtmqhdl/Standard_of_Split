package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.ceil

class Calculator : ViewModel() {

    private val _buttonStates = MutableLiveData(List(9) { false })
    val buttonStates: LiveData<List<Boolean>> = _buttonStates

    fun toggleButtonState(index: Int) {
        _buttonStates.value = _buttonStates.value?.toMutableList()?.apply {
            this[index] = !this[index]
        }
    }

    fun resetButtonStates() {
        _buttonStates.value = List(9) { false }
    }

    private val _changeMode = MutableLiveData(false)
    val changeMode: LiveData<Boolean> = _changeMode

    fun toggleChangeMode() {
        _changeMode.value = _changeMode.value?.not()
    }

    private val initialMap: MutableMap<Int, MutableMap<String, MutableMap<String, Int>>> =
        (1..8).associate { it to mutableMapOf<String, MutableMap<String, Int>>() }.toMutableMap()

    private val _personPay =
        MutableLiveData<MutableMap<Int, MutableMap<String, MutableMap<String, Int>>>>(initialMap)
    val personPay: LiveData<MutableMap<Int, MutableMap<String, MutableMap<String, Int>>>> =
        _personPay

    fun updatePersonPay(lst: List<Int>, placeName: String, productName: String, productPrice: Int) {
        try {
            val dividedPrice = (ceil((productPrice.toDouble() / lst.size) / 10) * 10).toInt()

            val updatedPersonPay = _personPay.value?.toMutableMap() ?: return

            for (i in lst) {
                val current = updatedPersonPay[i] ?: mutableMapOf()
                val updatedProducts = current[placeName]?.toMutableMap() ?: mutableMapOf()
                updatedProducts[productName] = dividedPrice
                current[placeName] = updatedProducts
                updatedPersonPay[i] = current
            }

            _personPay.value = updatedPersonPay

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

    private val _Key = MutableLiveData<Int>(0)
    val Key: LiveData<Int> = _Key

    private val _KeyKey = MutableLiveData<Int>(0)
    val KeyKey: LiveData<Int> = _KeyKey

    fun incrementKey() {
        _Key.value = (_Key.value ?: 0) + 1
    }

    fun incrementKeyKey() {
        _KeyKey.value = (_KeyKey.value ?: 0) + 1
    }

    fun decrementKey() {
        _Key.value = (_Key.value ?: 0) - 1
    }

    fun decrementKeyKey() {
        _KeyKey.value = (_KeyKey.value ?: 0) - 1
    }

    fun resetKeyKey() {
        _KeyKey.value = 0
    }

    val _showToastEvent = MutableLiveData<Boolean>()
    val showToastEvent: LiveData<Boolean> = _showToastEvent

    fun reDo() {
        val currentStack = _stack.value ?: mutableListOf()
        if (currentStack.isNotEmpty()) {
            decrementKeyKey()
            if (_KeyKey.value == -1) {
                decrementKey()
                _KeyKey.value = 0
            }
            val lastElement = currentStack.removeAt(currentStack.size - 1)
            _personPay.value =
                lastElement as? MutableMap<Int, MutableMap<String, MutableMap<String, Int>>>
            _stack.value = currentStack
        } else {
            _showToastEvent.value = true
        }
    }

    private val _updateTotalEvent = MutableLiveData<Boolean>()
    val updateTotalEvent: LiveData<Boolean> = _updateTotalEvent

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

    fun resetPersonPay() {
        _personPay.value = initialMap
        _stack.value = mutableListOf()
        _Key.value = 0
        _KeyKey.value = 0
    }

}