package com.mastermind.wordwise.domain.repository

import com.mastermind.wordwise.domain.model.TestResult
import com.mastermind.wordwise.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface TestRepository {
    suspend fun saveTestResult(correctAnswers: Int, totalQuestions: Int, incorrectWords: List<Word>): TestResult
    suspend fun getTestResults(): Flow<List<TestResult>>
} 