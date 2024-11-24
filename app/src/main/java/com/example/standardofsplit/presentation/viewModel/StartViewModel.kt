package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.domain.useCase.StartUseCase
import com.example.standardofsplit.presentation.event.StartEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val startUseCase: StartUseCase
) : ViewModel() {

    private val _personCount = MutableStateFlow(DEFAULT_PERSON_COUNT)
    val personCount: StateFlow<Int> = _personCount.asStateFlow()

    fun onEvent(event: StartEvent) {
        when (event) {
            is StartEvent.OnIncrement -> {
                _personCount.value = startUseCase.incrementPersonCount(_personCount.value)
            }
            is StartEvent.OnDecrement -> {
                _personCount.value = startUseCase.decrementPersonCount(_personCount.value)
            }
        }
    }

    companion object {
        const val DEFAULT_PERSON_COUNT = 2
    }
}