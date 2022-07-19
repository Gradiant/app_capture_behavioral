package com.example.usecases

import com.example.data.FirebaseRepository
import com.example.domain.Data

class SaveDataFirebase(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke(data: Data) = firebaseRepository.saveData(data)
}