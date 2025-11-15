package com.example.carddeck.domain.repository

import com.example.carddeck.domain.model.Session

interface SessionRepository {
    suspend fun createSession(): Session
    suspend fun findSessionByCode(code: String): Session?
}
