package com.example.carddeck.domain.repository

import kotlinx.coroutines.flow.Flow

interface ResponseRepository {
    suspend fun submitResponse(sessionCode: String, emotionIds: List<String>)
    fun observeAggregatedResponses(sessionCode: String): Flow<Map<String, Int>>
}
