package com.example.standardofsplit.presentation.event

sealed class StartScreenEvent {
    object OnIncrementClick : StartScreenEvent()
    object OnDecrementClick : StartScreenEvent()
    object OnStartClick : StartScreenEvent()
}