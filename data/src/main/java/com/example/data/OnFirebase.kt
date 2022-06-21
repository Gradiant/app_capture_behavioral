package com.example.data

import com.example.domain.Audio
import com.example.domain.Response
import com.example.domain.User
import kotlinx.coroutines.flow.Flow

interface OnFirebase {
    fun saveAudio(audio: Audio): Flow<Response<Any>>
    fun saveUserConsent(user: User): Flow<Response<Any>>
    fun loadEmailsRegistered(): Flow<Response<Any>>
}
