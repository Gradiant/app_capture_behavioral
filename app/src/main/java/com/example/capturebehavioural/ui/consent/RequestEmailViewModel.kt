package com.example.capturebehavioural.ui.consent

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.capturebehavioural.framework.FirebaseDatabase
import com.example.data.FirebaseRepository
import com.example.domain.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RequestEmailViewModel : ViewModel() {

   // private val loadEmails = LoadEmailsFirebase(FirebaseRepository(FirebaseDatabase()))

    private val _requestEmailState : MutableStateFlow<RequestEmailState?> = MutableStateFlow(null)
    val requestEmailState: StateFlow<RequestEmailState?> get() = _requestEmailState

    private val _responseState: MutableStateFlow<Response<Any>?> = MutableStateFlow(null)
    val responseState: StateFlow<Response<Any>?> get() = _responseState

    fun consentAcept(email: String) {
        if (email.isNullOrEmpty()) {
            viewModelScope.launch {
                _requestEmailState.value = RequestEmailState.NameError
            }
        } else if (!validarEmail(email)) {
            viewModelScope.launch {
                _requestEmailState.value = RequestEmailState.FormatError
            }
        } else {
            viewModelScope.launch {
                /*loadEmails.invoke().collect {
                    when(it) {
                        is Response.Success -> _responseState.value = Response.Success(it.data as List<String>)
                        is Response.Error -> _responseState.value = Response.Error(it.message)
                        else ->  _responseState.value = Response.Error("Error desconocido")
                    }
                }*/
            }
        }
    }

    private fun validarEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun confirmEmail(listOfEmail: List<String>, email: String) {
      /*  if (listOfEmail.contains(email)) {
            _requestEmailState.value = RequestEmailState.EmailRegistered
        } else {
            _requestEmailState.value = RequestEmailState.NewEmail
        }*/
    }

    class MainViewModelFactory: ViewModelProvider.NewInstanceFactory()
}