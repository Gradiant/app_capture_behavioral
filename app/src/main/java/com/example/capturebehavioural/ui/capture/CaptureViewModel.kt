package com.example.capturebehavioural.ui.capture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capturebehavioural.framework.FirebaseDatabase
import com.example.data.FirebaseRepository
import com.example.domain.Response
import com.example.usecases.SaveDataFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CaptureViewModel: ViewModel() {

    private val saveData = SaveDataFirebase(FirebaseRepository(FirebaseDatabase()))

    private val _captureState : MutableStateFlow<CaptureState?> = MutableStateFlow(null)
    val captureState: StateFlow<CaptureState?> get() = _captureState

    private val _responseState : MutableStateFlow<Response<Any>?> = MutableStateFlow(null)
    val responseState: StateFlow<Response<Any>?> get() = _responseState

    fun clickStart() {
        _captureState.value = CaptureState.Start
    }

    fun clickNext() {
        _captureState.value = CaptureState.Next
    }

    fun clickBack() {
        _captureState.value = CaptureState.Back
    }

    fun clickCapture() {
        _captureState.value = CaptureState.Capture
    }

    class MainViewModelFactory :
        ViewModelProvider.NewInstanceFactory()
}