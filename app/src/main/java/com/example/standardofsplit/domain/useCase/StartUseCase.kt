package com.example.standardofsplit.domain.useCase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class StartUseCase @Inject constructor() {
    private val _personCount = MutableStateFlow(DEFAULT_PERSON_COUNT)
    val personCount: StateFlow<Int> = _personCount.asStateFlow()

    fun incrementPersonCount(): Boolean {
        val currentCount = _personCount.value
        return if (currentCount < MAX_PERSON_COUNT) {
            _personCount.value = currentCount + 1
            true
        } else {
            false
        }
    }

    fun decrementPersonCount(): Boolean {
        val currentCount = _personCount.value
        return if (currentCount > MIN_PERSON_COUNT) {
            _personCount.value = currentCount - 1
            true
        } else {
            false
        }
    }

    companion object {
        const val DEFAULT_PERSON_COUNT = 2
        const val MIN_PERSON_COUNT = 2
        const val MAX_PERSON_COUNT = 8
    }
}

