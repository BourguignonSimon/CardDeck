package com.example.carddeck.domain.model

import com.google.firebase.Timestamp

data class Session(
    val id: String,
    val code: String,
    val createdAt: Timestamp
)
