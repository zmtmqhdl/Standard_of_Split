package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Start : ViewModel() {
    private val _personCount = MutableLiveData(2)  // 기본값을 2로 설정
    val personCount: LiveData<Int> = _personCount

    fun increment() {
        if (_personCount.value ?: 2 < 8) {
            _personCount.value = (_personCount.value ?: 2) + 1
        }
    }

    fun decrement() {
        if (_personCount.value ?: 2 > 2) {
            _personCount.value = (_personCount.value ?: 2) - 1
        }
    }
}