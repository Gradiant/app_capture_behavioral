package com.example.capturebehavioural.ui.capture

sealed class CaptureState {
    object Start: CaptureState()
    object Next: CaptureState()
    object Back: CaptureState()
    object Capture: CaptureState()
    object Dialog: CaptureState()
}