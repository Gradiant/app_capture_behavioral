package com.example.capturebehavioural.ui.consent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConsentDetailViewModel : ViewModel() {

    private val _consentDetailState : MutableStateFlow<ConsentDetailState?> = MutableStateFlow(null)
    val consentDetailState: StateFlow<ConsentDetailState?> get() = _consentDetailState

    fun onButtonClicked() {
        _consentDetailState.value = ConsentDetailState.Next
    }

    class MainViewModelFactory :
        ViewModelProvider.NewInstanceFactory()
}