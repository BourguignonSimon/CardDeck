package com.example.carddeck.data.repository

import com.example.carddeck.domain.model.Session
import com.example.carddeck.domain.repository.SessionRepository
import com.example.carddeck.core.utils.SessionCodeGenerator
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class SessionRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionCodeGenerator: SessionCodeGenerator
) : SessionRepository {

    private val sessionsCollection get() = firestore.collection("sessions")

    override suspend fun createSession(): Session {
        repeat(MAX_ATTEMPTS) { _ ->
            val code = sessionCodeGenerator.generateCode()
            val existing = sessionsCollection.whereEqualTo("code", code).get().await()
            if (existing.isEmpty) {
                val docRef = sessionsCollection.document()
                val createdAt = Timestamp.now()
                val payload = mapOf(
                    FIELD_CODE to code,
                    FIELD_CREATED_AT to createdAt
                )
                docRef.set(payload).await()
                return Session(
                    id = docRef.id,
                    code = code,
                    createdAt = createdAt
                )
            }
        }
        throw IllegalStateException("Unable to create unique session code")
    }

    override suspend fun findSessionByCode(code: String): Session? {
        val snapshot = sessionsCollection.whereEqualTo(FIELD_CODE, code).limit(1).get().await()
        val document = snapshot.documents.firstOrNull() ?: return null
        val createdAt = document.getTimestamp(FIELD_CREATED_AT) ?: Timestamp.now()
        return Session(
            id = document.id,
            code = document.getString(FIELD_CODE) ?: code,
            createdAt = createdAt
        )
    }

    private companion object {
        const val MAX_ATTEMPTS = 10
        const val FIELD_CODE = "code"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
