package com.example.capturebehavioural.ui.capture

import android.content.Context
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import com.example.capturebehavioural.framework.FirebaseDatabase
import com.example.data.FirebaseRepository
import com.example.domain.Response
import com.example.usecases.SaveDataFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CaptureViewModel: ViewModel() {



    private val saveData = SaveDataFirebase(FirebaseRepository(FirebaseDatabase()))

    private val _recordState : MutableStateFlow<CaptureState?> = MutableStateFlow(null)
    val recordState: StateFlow<CaptureState?> get() = _recordState

    private val _responseState : MutableStateFlow<Response<Any>?> = MutableStateFlow(null)
    val responseState: StateFlow<Response<Any>?> get() = _responseState
}