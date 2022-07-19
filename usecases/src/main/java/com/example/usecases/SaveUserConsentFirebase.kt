package com.example.usecases

import com.example.data.FirebaseRepository
import com.example.domain.User

class SaveUserConsentFirebase(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke(user: User) = firebaseRepository.saveUserConsent(user)
}