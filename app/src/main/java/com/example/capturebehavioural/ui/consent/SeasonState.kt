package com.example.capturebehavioural.ui.consent

sealed class SeasonState {
    object NewUser: SeasonState()
    object NewSeason: SeasonState()
}