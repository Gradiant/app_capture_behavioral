package com.example.capturebehavioural.ui.consent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SeasonViewModel : ViewModel() {

    private val _seasonState : MutableStateFlow<SeasonState?> = MutableStateFlow(null)
    val seasonState: StateFlow<SeasonState?> get() = _seasonState

    fun clickNewSeason() {
        viewModelScope.launch {
            _seasonState.value = SeasonState.NewSeason
        }
    }

    fun clickNewUser() {
        viewModelScope.launch {
            _seasonState.value = SeasonState.NewUser
        }
    }

    class MainViewModelFactory: ViewModelProvider.NewInstanceFactory()


    fun onResume() {
        _seasonState.value = null
    }
}