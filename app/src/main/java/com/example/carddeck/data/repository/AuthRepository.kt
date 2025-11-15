package com.example.carddeck.data.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun ensureAnonymousUser() {
        if (firebaseAuth.currentUser == null) {
            firebaseAuth.signInAnonymously().await()
        }
    }
}
