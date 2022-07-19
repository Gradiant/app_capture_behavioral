package com.example.capturebehavioural.ui.capture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.capturebehavioural.framework.FirebaseDatabase
import com.example.data.FirebaseRepository
import com.example.domain.Data
import com.example.domain.Response
import com.example.usecases.SaveDataFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class CaptureViewModel: ViewModel() {

    private var iteration = 0

    private val saveData = SaveDataFirebase(FirebaseRepository(FirebaseDatabase()))

    private val _captureState : MutableStateFlow<CaptureState?> = MutableStateFlow(null)
    val captureState: StateFlow<CaptureState?> get() = _captureState

    private val _responseState : MutableStateFlow<Response<Any>?> = MutableStateFlow(null)
    val responseState: StateFlow<Response<Any>?> get() = _responseState

    fun clickStart() {
        _captureState.value = CaptureState.Start
    }

    fun saveValues(user: String, season: String, sensor: String, file: File) {
        viewModelScope.launch {
            saveData.invoke(Data(file.path, season, sensor, user)).collect {
                when(it) {
                    is Response.Success -> {
                        _responseState.value = Response.Success(it)
                    }
                    else -> {
                        _responseState.value =
                            Response.Error("Error desconocido")
                    }
                }
            }
        }
    }

    fun clickNext() {
        _captureState.value = CaptureState.Next
    }

    fun clickAcept(isChecked: Boolean) {
        if (iteration == 0) {
            _captureState.value = CaptureState.Dialog
            iteration++
            _captureState.value = CaptureState.Checked(isChecked)
        } else {
            _captureState.value = CaptureState.Checked(isChecked)
        }
    }

    fun clickCapture() {
        if (iteration <= 4) {
            _captureState.value = CaptureState.Capture
            iteration++
        } else {
            _captureState.value = CaptureState.Finish
        }
    }

    fun clickCont() {
        _captureState.value = CaptureState.Form
    }

    fun clickFinish() {
        _captureState.value = null
        _captureState.value = CaptureState.Navigation
    }

    fun clickRecCapt() {
        _captureState.value = CaptureState.RecCap
    }

    fun clickOkResults() {
        _captureState.value = CaptureState.ResultsOk
    }

    fun showDialog() {
        _captureState.value = null
        _captureState.value = CaptureState.Dialog
    }

    class MainViewModelFactory :
        ViewModelProvider.NewInstanceFactory()
}