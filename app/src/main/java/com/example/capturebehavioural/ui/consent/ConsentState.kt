package com.example.capturebehavioural.ui.consent

sealed class ConsentState {
    class InfoOK(val consent: Int): ConsentState()
    class Consent1(val consent: Int): ConsentState()
    class Consent2(val consent: Int): ConsentState()
    class Consent3(val consent: Int): ConsentState()
    class Consent4(val consent: Int): ConsentState()
    object AllConsentOk: ConsentState()
    object Error: ConsentState()
}
