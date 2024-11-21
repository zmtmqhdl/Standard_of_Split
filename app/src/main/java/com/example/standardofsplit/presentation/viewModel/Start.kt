package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.domain.useCase.StartUseCase
import com.example.standardofsplit.presentation.event.StartScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class Start @Inject constructor(
    private val startUseCase: StartUseCase
) : ViewModel() {
    
    val personCount: StateFlow<Int> = startUseCase.personCount

    fun onEvent(event: StartScreenEvent) {
        when (event) {
            is StartScreenEvent.OnIncrementClick -> startUseCase.incrementPersonCount()
            is StartScreenEvent.OnDecrementClick -> startUseCase.decrementPersonCount()
            is StartScreenEvent.OnStartClick -> { /* 처리 */ }
        }
    }
}