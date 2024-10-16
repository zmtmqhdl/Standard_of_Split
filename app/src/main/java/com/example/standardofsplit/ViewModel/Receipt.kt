package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.Model.items

class Receipt: ViewModel() {
    private val _items = MutableLiveData<List<items>>(listOf())
    val items: LiveData<List<items>> = _items

    // 영수증 추가 기능
    fun addItem() {
        val currentItems = _items.value ?: listOf()

        // 새로운 영수증 항목 추가
        val newItem = items(
            ReceiptName = "Receipt ${currentItems.size + 1}",
            MenuName = mutableListOf("Menu ${currentItems.size + 1}", "Menu ${currentItems.size + 2}"),
            MenuQuantity = mutableListOf("${currentItems.size + 1}개", "${currentItems.size + 2}개"),
            MenuPrice = mutableListOf("${(currentItems.size + 1) * 1000}원", "${(currentItems.size + 2) * 1000}원")
        )

        _items.value = currentItems + newItem  // 리스트에 새로운 항목 추가
    }
}