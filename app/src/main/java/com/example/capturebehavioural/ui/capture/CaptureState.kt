package com.example.capturebehavioural.ui.capture

sealed class CaptureState {
    object Start: CaptureState()
    object Next: CaptureState()
    class Checked(val isChecked: Boolean): CaptureState()
    object Capture: CaptureState()
    object RecCap: CaptureState()
    object Dialog: CaptureState()
    object Form: CaptureState()
    object Finish: CaptureState()
    object Navigation: CaptureState()
    object ResultsOk: CaptureState()

}