package com.example.standardofsplit.ViewModel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Calculator : ViewModel() {
    private val _changeMode = MutableLiveData(false)
    val changeMode: LiveData<Boolean> = _changeMode

    fun toggleChangeMode() {
        _changeMode.value = _changeMode.value?.not()
    }

    private val _personPay = MutableLiveData<Map<String, Map<String, Map<String, Int>>>>()
    val personPay: LiveData<Map<String, Map<String, Map<String, Int>>>> = _personPay

}