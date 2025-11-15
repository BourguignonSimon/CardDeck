package com.example.carddeck.di

import com.example.carddeck.data.repository.ResponseRepositoryImpl
import com.example.carddeck.data.repository.SessionRepositoryImpl
import com.example.carddeck.domain.repository.ResponseRepository
import com.example.carddeck.domain.repository.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindResponseRepository(impl: ResponseRepositoryImpl): ResponseRepository
}
