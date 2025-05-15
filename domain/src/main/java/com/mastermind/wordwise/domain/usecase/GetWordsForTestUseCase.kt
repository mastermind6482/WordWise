package com.mastermind.wordwise.domain.usecase

import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.model.Word
import com.mastermind.wordwise.domain.repository.WordRepository
import javax.inject.Inject

class GetWordsForTestUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {
    suspend operator fun invoke(level: LanguageLevel, count: Int = 10): List<Word> {
        return wordRepository.getWordsForTest(level, count)
    }
} 