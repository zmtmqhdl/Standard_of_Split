package com.example.standardofsplit.domain.useCase

import javax.inject.Inject

class StartUseCase @Inject constructor() {
    fun incrementPersonCount(currentCount: Int): Int {
        return if (currentCount < MAX_PERSON_COUNT) {
            currentCount + 1
        } else {
            currentCount
        }
    }

    fun decrementPersonCount(currentCount: Int): Int {
        return if (currentCount > MIN_PERSON_COUNT) {
            currentCount - 1
        } else {
            currentCount
        }
    }

    companion object {
        const val MIN_PERSON_COUNT = 2
        const val MAX_PERSON_COUNT = 8
    }
}