package com.example.capturebehavioural.ui.consent

sealed class RequestEmailState {
    object EmailRegistered: RequestEmailState()
    object NewEmail: RequestEmailState()
    object NameError: RequestEmailState()
    object FormatError: RequestEmailState()
}