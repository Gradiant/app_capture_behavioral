package com.example.capturebehavioural.ui.consent

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.capturebehavioural.framework.FirebaseDatabase
import com.example.data.FirebaseRepository
import com.example.domain.Consents
import com.example.domain.Response
import com.example.domain.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
class ConsentViewModel: ViewModel() {

    private var consent = 0

    //private val saveUserConsent = SaveUserConsentFirebase(FirebaseRepository(FirebaseDatabase()))

    private val _consentState : MutableStateFlow<ConsentState?> = MutableStateFlow(null)
    val consentState: StateFlow<ConsentState?> get() = _consentState

    private val _responseState : MutableStateFlow<Response<Any>?> = MutableStateFlow(null)
    val responseState: StateFlow<Response<Any>?> get() = _responseState

    fun onButtonClicked(con: Int, consent1: Boolean, consent2: Boolean, consent3: Boolean, consent4: Boolean, consent5: Boolean) {
        consent = con
        viewModelScope.launch {
            when(consent) {
                0 -> {
                    consent += 1
                    _consentState.value = ConsentState.InfoOK(consent)
                }
                1 -> {
                    if(consent1) {
                        consent += 1
                        _consentState.value = ConsentState.Consent1(consent)
                    } else {
                        _consentState.value = ConsentState.Error
                    }
                }
                2 -> {
                    if(consent2) {
                        consent += 1
                        _consentState.value = ConsentState.Consent2(consent)
                    } else {
                        _consentState.value = ConsentState.Error
                    }
                }
                3 -> {
                    if(consent3) {
                        consent += 1
                        _consentState.value = ConsentState.Consent3(consent)
                    } else {
                        _consentState.value = ConsentState.Error
                    }
                }

                4 -> {
                    if(consent4) {
                        consent += 1
                        _consentState.value = ConsentState.Consent4(consent)
                    } else {
                        _consentState.value = ConsentState.Error
                    }
                }

                5 -> {
                    if(consent5) {
                        consent += 1
                        _consentState.value = ConsentState.AllConsentOk
                    } else {
                        _consentState.value = ConsentState.Error
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun sendUserConsent(name: String, lastName: String, address: String, phone: String, email: String) {
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val date = sdf.format(Date())
        viewModelScope.launch {
        /*    saveUserConsent.invoke(User( name,
                lastName,
                address,
                phone,
                email,
                date,
                "0",
                consents = Consents(
                    consentimiento_captura = true,
                    consentimiento_voluntario = true,
                    consentimiento_publicacion = true,
                    consentimiento_administracion = true,
                    consentimiento_acceso_correcion_eliminacion = true
                )
            )).collect {
                when(it) {
                    is Response.Success -> {
                        _responseState.value = Response.Success(it)
                    }
                    else -> {
                        _responseState.value =
                            Response.Error("Error desconocido")
                    }
                }
            }*/
        }
    }

    class MainViewModelFactory : ViewModelProvider.NewInstanceFactory()
}