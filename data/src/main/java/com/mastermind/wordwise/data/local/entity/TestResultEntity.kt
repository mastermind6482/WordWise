package com.mastermind.wordwise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mastermind.wordwise.domain.model.TestResult
import com.mastermind.wordwise.domain.model.Word

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey
    val id: String,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timestamp: Long,
    // We'll store the incorrect word IDs as a comma-separated string
    val incorrectWordIds: String
)

fun TestResultEntity.toDomain(incorrectWords: List<Word>): TestResult {
    return TestResult(
        id = id,
        correctAnswers = correctAnswers,
        totalQuestions = totalQuestions,
        timestamp = timestamp,
        incorrectWords = incorrectWords
    )
}

fun TestResult.toEntity(): TestResultEntity {
    return TestResultEntity(
        id = id,
        correctAnswers = correctAnswers,
        totalQuestions = totalQuestions,
        timestamp = timestamp,
        incorrectWordIds = incorrectWords.joinToString(",") { it.id }
    )
} 