package com.mastermind.wordwise.domain.repository

import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(name: String, level: LanguageLevel): User
    suspend fun updateUser(user: User)
    suspend fun getUser(): Flow<User?>
    suspend fun updateUserLevel(level: LanguageLevel)
    suspend fun updateUserStats(wordsLearned: Int = 0, correctAnswers: Int = 0, totalAnswers: Int = 0)
    suspend fun resetProgress()
} 