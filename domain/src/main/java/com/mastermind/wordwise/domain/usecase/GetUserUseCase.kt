package com.mastermind.wordwise.domain.usecase

import com.mastermind.wordwise.domain.model.User
import com.mastermind.wordwise.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<User?> {
        return userRepository.getUser()
    }
} 