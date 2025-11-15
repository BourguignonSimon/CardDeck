package com.example.carddeck.domain.model

import com.google.firebase.Timestamp

data class Response(
    val id: String,
    val sessionCode: String,
    val emotionIds: List<String>,
    val createdAt: Timestamp
)
