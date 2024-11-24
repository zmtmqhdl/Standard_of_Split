package com.example.standardofsplit.presentation.event

sealed class StartEvent {
    data object OnIncrement : StartEvent()
    data object OnDecrement : StartEvent()
}