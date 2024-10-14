package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Receipt: ViewModel() {
    private val _receiptCount = MutableLiveData(0)
    val receiptCount: LiveData<Int> = _receiptCount

    private val _menuName = MutableLiveData("")
    val menuName: LiveData<String> = _menuName

    private val _menuQuantity = MutableLiveData("1")
    val menuQuantity: LiveData<String> = _menuQuantity

    private val _menuPrice = MutableLiveData("")
    val menuPrice: LiveData<String> = _menuPrice

    private val _items = MutableLiveData<List<Triple<String, String, String>>>(listOf())
    val items: LiveData<List<Triple<String, String, String>>> = _items


    fun increment() {
        _receiptCount.value = (_receiptCount.value ?: 1) + 1
        updateItems()
    }

    private fun updateItems() {
        val _receiptCount = _receiptCount.value ?: 1
        _items.value = List(_receiptCount) { Triple("Menu $it", "${(it + 1)}개", "${(it + 1) * 1000}원") }
    }
}