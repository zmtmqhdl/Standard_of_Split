package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : ViewModel() {

    private val _personCount = MutableStateFlow(2)
    val personCount: StateFlow<Int> = _personCount.asStateFlow()

    fun incrementCount() {
        if (_personCount.value < MAX_PERSON_COUNT) {
            _personCount.value += 1
        }
    }

    fun decrementCount() {
        if (_personCount.value > MIN_PERSON_COUNT) {
            _personCount.value -= 1
        }
    }

    companion object {
        const val MIN_PERSON_COUNT = 2
        const val MAX_PERSON_COUNT = 8
    }
}