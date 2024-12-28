package com.example.standardofsplit.presentation.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.presentation.ui.component.showCustomToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : ViewModel() {

    private val _personCount = MutableStateFlow(2)
    val personCount: StateFlow<Int> = _personCount.asStateFlow()

    private val _backPressedTime = MutableStateFlow(0L)

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

    fun exit(context: Context) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - _backPressedTime.value > 2000) {
            _backPressedTime.value = currentTime
            showCustomToast(context, "뒤로가기 키를 누르면 종료됩니다.")
        } else {
            (context as? AppCompatActivity)?.finish()
        }
    }

    companion object {
        const val MIN_PERSON_COUNT = 2
        const val MAX_PERSON_COUNT = 8
    }
}