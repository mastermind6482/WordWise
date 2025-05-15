package com.mastermind.wordwise.domain.usecase

import com.mastermind.wordwise.domain.repository.WordRepository
import javax.inject.Inject

class MarkWordForRepetitionUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {
    suspend operator fun invoke(wordId: String, needsRepetition: Boolean) {
        wordRepository.markWordForRepetition(wordId, needsRepetition)
    }
} 