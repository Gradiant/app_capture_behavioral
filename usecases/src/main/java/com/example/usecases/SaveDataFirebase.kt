package com.example.usecases

import com.example.data.FirebaseRepository
import com.example.domain.Audio

class SaveDataFirebase(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke(audio: Audio) = firebaseRepository.saveAudio(audio)
}