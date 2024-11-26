package com.example.standardofsplit.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.standardofsplit.domain.useCase.StartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val startUseCase: StartUseCase
) : ViewModel() {

    private val _personCount = MutableStateFlow(2)
    val personCount: StateFlow<Int> = _personCount.asStateFlow()

    fun incrementCount() {
        _personCount.value = startUseCase.incrementPersonCount(_personCount.value)
    }

    fun decrementCount() {
        _personCount.value = startUseCase.decrementPersonCount(_personCount.value)
    }
}