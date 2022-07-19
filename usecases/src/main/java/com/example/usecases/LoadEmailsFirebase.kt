package com.example.usecases

import com.example.data.FirebaseRepository

class LoadEmailsFirebase(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke() = firebaseRepository.loadEmailsRegistered()
}