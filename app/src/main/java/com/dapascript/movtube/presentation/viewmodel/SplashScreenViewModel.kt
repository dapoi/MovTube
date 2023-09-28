package com.dapascript.movtube.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor() : ViewModel() {

    private val _keepRunning = MutableStateFlow(true)
    val keepRunning = _keepRunning.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            _keepRunning.value = false
        }
    }
}