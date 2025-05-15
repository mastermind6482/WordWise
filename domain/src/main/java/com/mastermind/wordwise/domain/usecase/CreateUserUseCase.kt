package com.mastermind.wordwise.domain.usecase

import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.model.User
import com.mastermind.wordwise.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, level: LanguageLevel): User {
        return userRepository.createUser(name, level)
    }
} 