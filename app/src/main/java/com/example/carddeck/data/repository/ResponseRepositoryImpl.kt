package com.example.carddeck.data.repository

import com.example.carddeck.domain.repository.ResponseRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ResponseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ResponseRepository {

    private val responsesCollection get() = firestore.collection("responses")

    override suspend fun submitResponse(sessionCode: String, emotionIds: List<String>) {
        val data = mapOf(
            FIELD_SESSION_CODE to sessionCode,
            FIELD_EMOTION_IDS to emotionIds,
            FIELD_CREATED_AT to Timestamp.now()
        )
        responsesCollection.add(data).await()
    }

    override fun observeAggregatedResponses(sessionCode: String): Flow<Map<String, Int>> = callbackFlow {
        var registration: ListenerRegistration? = null
        registration = responsesCollection.whereEqualTo(FIELD_SESSION_CODE, sessionCode)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyMap())
                    return@addSnapshotListener
                }
                val counts = mutableMapOf<String, Int>()
                snapshot?.documents.orEmpty().forEach { document ->
                    val ids = document.get(FIELD_EMOTION_IDS) as? List<*>
                    ids?.forEach { id ->
                        val key = id?.toString() ?: return@forEach
                        counts[key] = (counts[key] ?: 0) + 1
                    }
                }
                trySend(counts)
            }
        awaitClose { registration?.remove() }
    }

    private companion object {
        const val FIELD_SESSION_CODE = "sessionCode"
        const val FIELD_EMOTION_IDS = "emotionIds"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
