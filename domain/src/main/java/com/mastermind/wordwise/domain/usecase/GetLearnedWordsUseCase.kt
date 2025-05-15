package com.mastermind.wordwise.domain.usecase

import com.mastermind.wordwise.domain.model.Word
import com.mastermind.wordwise.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLearnedWordsUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {
    suspend operator fun invoke(): Flow<List<Word>> {
        return wordRepository.getLearnedWords()
    }
} 