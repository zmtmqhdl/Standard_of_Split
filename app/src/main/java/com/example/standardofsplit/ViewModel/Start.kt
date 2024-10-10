package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Start : ViewModel() {
    private val personCount = MutableLiveData(2)  // 기본값을 2로 설정
    val count: LiveData<Int> = personCount

    fun increment() {
        if (personCount.value ?: 2 < 8) {
            personCount.value = (personCount.value ?: 2) + 1
        }
    }

    fun decrement() {
        if (personCount.value ?: 2 > 2) {
            personCount.value = (personCount.value ?: 2) - 1
        }
    }
}