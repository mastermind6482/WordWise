package com.mastermind.wordwise.data.repository

import com.mastermind.wordwise.data.local.dao.TestResultDao
import com.mastermind.wordwise.data.local.dao.WordDao
import com.mastermind.wordwise.data.local.entity.toDomain
import com.mastermind.wordwise.data.local.entity.toEntity
import com.mastermind.wordwise.domain.model.TestResult
import com.mastermind.wordwise.domain.model.Word
import com.mastermind.wordwise.domain.repository.TestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testResultDao: TestResultDao,
    private val wordDao: WordDao
) : TestRepository {
    override suspend fun saveTestResult(
        correctAnswers: Int,
        totalQuestions: Int,
        incorrectWords: List<Word>
    ): TestResult {
        val testResult = TestResult(
            id = UUID.randomUUID().toString(),
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            timestamp = System.currentTimeMillis(),
            incorrectWords = incorrectWords
        )
        
        testResultDao.insertTestResult(testResult.toEntity())
        return testResult
    }

    override suspend fun getTestResults(): Flow<List<TestResult>> {
        return testResultDao.getTestResults().map { entities ->
            entities.map { entity ->
                val wordIds = entity.incorrectWordIds.split(",").filter { it.isNotEmpty() }
                val incorrectWords = if (wordIds.isNotEmpty()) {
                    wordDao.getWordsByIds(wordIds).map { it.toDomain() }
                } else {
                    emptyList()
                }
                entity.toDomain(incorrectWords)
            }
        }
    }
} 