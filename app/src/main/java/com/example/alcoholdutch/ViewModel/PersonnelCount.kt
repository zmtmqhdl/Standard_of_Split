package com.example.alcoholdutch.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonnelCount : ViewModel() {
    private val personcount = MutableLiveData(2)  // 기본값을 2로 설정
    val count: LiveData<Int> = personcount

    fun increment() {
        if (personcount.value ?: 2 < 8) {
            personcount.value = (personcount.value ?: 2) + 1
        }
    }

    fun decrement() {
        if (personcount.value ?: 2 > 2) {
            personcount.value = (personcount.value ?: 2) - 1
        }
    }
}