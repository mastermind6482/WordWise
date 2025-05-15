package com.mastermind.wordwise.domain.usecase

import com.mastermind.wordwise.domain.model.TestResult
import com.mastermind.wordwise.domain.model.Word
import com.mastermind.wordwise.domain.repository.TestRepository
import com.mastermind.wordwise.domain.repository.UserRepository
import com.mastermind.wordwise.domain.repository.WordRepository
import javax.inject.Inject

class SaveTestResultUseCase @Inject constructor(
    private val testRepository: TestRepository,
    private val userRepository: UserRepository,
    private val wordRepository: WordRepository
) {
    suspend operator fun invoke(correctAnswers: Int, totalQuestions: Int, incorrectWords: List<Word>, allWords: List<Word>): TestResult {
        // Update user stats
        userRepository.updateUserStats(
            wordsLearned = correctAnswers,
            correctAnswers = correctAnswers,
            totalAnswers = totalQuestions
        )
        // Mark correct words as learned
        val correctWords = allWords.filter { word -> incorrectWords.none { it.id == word.id } }
        correctWords.forEach { word ->
            wordRepository.markWordAsLearned(word.id)
        }
        return testRepository.saveTestResult(correctAnswers, totalQuestions, incorrectWords)
    }
}