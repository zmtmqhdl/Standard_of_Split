package com.example.standardofsplit.domain.usecase

import javax.inject.Inject

class StartUseCase @Inject constructor() {
    fun incrementPersonCount(currentCount: Int) {
        if (currentCount < MAX_PERSON_COUNT) {
            currentCount + 1
        }
    }

    fun decrementPersonCount(currentCount: Int) {
        if (currentCount > MIN_PERSON_COUNT) {
            currentCount - 1
        }
    }

    companion object {
        const val MIN_PERSON_COUNT = 2
        const val MAX_PERSON_COUNT = 8
    }
}