package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Calculator : ViewModel() {
    private val _changeMode = MutableLiveData(false)
    val changeMode: LiveData<Boolean> = _changeMode

    fun toggleChangeMode() {
        _changeMode.value = _changeMode.value?.not()
    }
}