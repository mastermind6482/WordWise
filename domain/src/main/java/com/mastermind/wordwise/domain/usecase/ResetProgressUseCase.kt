package com.mastermind.wordwise.domain.usecase

import com.mastermind.wordwise.domain.repository.UserRepository
import com.mastermind.wordwise.domain.repository.WordRepository
import javax.inject.Inject

class ResetProgressUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val wordRepository: WordRepository
) {
    suspend operator fun invoke() {
        userRepository.resetProgress()
        wordRepository.resetAllWords()
    }
} 