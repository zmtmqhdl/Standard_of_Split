package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Start : ViewModel() {
    private val _personCount = MutableLiveData(DEFAULT_PERSON_COUNT)
    val personCount: LiveData<Int> = _personCount

    private val _showToast = MutableLiveData<Boolean>()
    val showToast: LiveData<Boolean> = _showToast

    private var isToastShowing = false

    fun increment() {
        val currentCount = _personCount.value ?: 2
        if (currentCount >= MAX_PERSON_COUNT && !isToastShowing) {
            isToastShowing = true
            _showToast.value = true
        } else if (currentCount < MAX_PERSON_COUNT) {
            _personCount.value = currentCount + 1
        }
    }

    fun decrement() {
        val currentCount = _personCount.value ?: 2
        if (currentCount <= MIN_PERSON_COUNT && !isToastShowing) {
            isToastShowing = true
            _showToast.value = true
        } else if (currentCount > MIN_PERSON_COUNT) {
            _personCount.value = currentCount - 1
        }
    }

    fun resetToast() {
        _showToast.value = false
        viewModelScope.launch {
            delay(2000) // 토스트 메시지 지속 시간
            isToastShowing = false
        }
    }

    companion object {
        const val DEFAULT_PERSON_COUNT = 2
        const val MIN_PERSON_COUNT = 2
        const val MAX_PERSON_COUNT = 8
    }
}