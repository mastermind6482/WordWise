package com.mastermind.wordwise.domain.repository

import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    suspend fun getWordsByLevel(level: LanguageLevel): Flow<List<Word>>
    suspend fun getLearnedWords(): Flow<List<Word>>
    suspend fun getWordsForTest(level: LanguageLevel, count: Int): List<Word>
    suspend fun markWordAsLearned(wordId: String)
    suspend fun markWordForRepetition(wordId: String, needsRepetition: Boolean)
    suspend fun resetAllWords()
} 