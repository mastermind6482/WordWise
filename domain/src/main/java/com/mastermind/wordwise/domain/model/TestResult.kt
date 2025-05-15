package com.mastermind.wordwise.domain.model

data class TestResult(
    val id: String,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timestamp: Long,
    val incorrectWords: List<Word>
) {
    val score: Float
        get() = correctAnswers.toFloat() / totalQuestions
} 