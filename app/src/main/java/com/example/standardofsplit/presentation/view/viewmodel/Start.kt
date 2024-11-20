package com.example.standardofsplit.presentation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Start : ViewModel() {
    private val _personCount = MutableLiveData(DEFAULT_PERSON_COUNT)
    val personCount: LiveData<Int> = _personCount

    fun increment() {
        val currentCount = _personCount.value ?: 2
        if (currentCount < MAX_PERSON_COUNT) {
            _personCount.value = currentCount + 1
        }
    }

    fun decrement() {
        val currentCount = _personCount.value ?: 2
        if (currentCount > MIN_PERSON_COUNT) {
            _personCount.value = currentCount - 1
        }
    }

    companion object {
        const val DEFAULT_PERSON_COUNT = 2
        const val MIN_PERSON_COUNT = 2
        const val MAX_PERSON_COUNT = 8
    }
}