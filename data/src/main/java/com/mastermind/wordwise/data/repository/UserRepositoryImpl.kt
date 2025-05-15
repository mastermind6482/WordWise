package com.mastermind.wordwise.data.repository

import com.mastermind.wordwise.data.local.UserPreferences
import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.model.User
import com.mastermind.wordwise.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences
) : UserRepository {
    override suspend fun createUser(name: String, level: LanguageLevel): User {
        val user = User(
            name = name,
            level = level
        )
        userPreferences.saveUser(user)
        return user
    }

    override suspend fun updateUser(user: User) {
        userPreferences.saveUser(user)
    }

    override suspend fun getUser(): Flow<User?> {
        return userPreferences.userFlow
    }

    override suspend fun updateUserLevel(level: LanguageLevel) {
        userPreferences.updateUserLevel(level)
    }

    override suspend fun updateUserStats(wordsLearned: Int, correctAnswers: Int, totalAnswers: Int) {
        userPreferences.updateUserStats(wordsLearned, correctAnswers, totalAnswers)
    }

    override suspend fun resetProgress() {
        userPreferences.resetProgress()
    }
} 