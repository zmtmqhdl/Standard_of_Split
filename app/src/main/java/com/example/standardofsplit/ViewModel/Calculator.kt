package com.example.standardofsplit.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

        val dividedPrice = (ceil((productPrice.toDouble() / lst.size) / 10) * 10).toInt()

        for (i in lst) {
            // personPay[i]가 없으면 새로 초기화
            val current = personPay.value?.get(i) ?: mutableMapOf()

            // placeName이 존재하면 상품 추가, 없으면 새로 추가
            val updatedProducts = current[placeName]?.toMutableMap() ?: mutableMapOf()
            updatedProducts[productName] = dividedPrice

            // current를 personPay[i]에 반영
            _personPay.value = _personPay.value?.toMutableMap()?.apply {
                put(i, current.apply { put(placeName, updatedProducts) })
            }
        }
        Log.d("test", _personPay.value.toString() )
    }
}