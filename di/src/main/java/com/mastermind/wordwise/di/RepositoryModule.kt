package com.mastermind.wordwise.di

import com.mastermind.wordwise.data.repository.TestRepositoryImpl
import com.mastermind.wordwise.data.repository.UserRepositoryImpl
import com.mastermind.wordwise.data.repository.WordRepositoryImpl
import com.mastermind.wordwise.domain.repository.TestRepository
import com.mastermind.wordwise.domain.repository.UserRepository
import com.mastermind.wordwise.domain.repository.WordRepository
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
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindWordRepository(
        wordRepositoryImpl: WordRepositoryImpl
    ): WordRepository
    
    @Binds
    @Singleton
    abstract fun bindTestRepository(
        testRepositoryImpl: TestRepositoryImpl
    ): TestRepository
} 