package com.example.data

import com.example.domain.Audio
import com.example.domain.Response
import com.example.domain.User
import kotlinx.coroutines.flow.Flow

class FirebaseRepository(private val onFirebase: OnFirebase) {

    fun saveAudio(audio: Audio) : Flow<Response<Any>> {
        return onFirebase.saveAudio(audio)
    }

    fun saveUserConsent(user: User) : Flow<Response<Any>> {
       return onFirebase.saveUserConsent(user)
    }

    fun loadEmailsRegistered(): Flow<Response<Any>> {
        return onFirebase.loadEmailsRegistered()
    }
}