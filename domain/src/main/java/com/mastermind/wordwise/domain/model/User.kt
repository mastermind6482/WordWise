package com.mastermind.wordwise.domain.model

enum class LanguageLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

data class User(
    val id: String = "user_1",
    val name: String,
    val level: LanguageLevel,
    val wordsLearned: Int = 0,
    val correctAnswers: Int = 0,
    val totalAnswers: Int = 0
) {
    val progressPercentage: Float
        get() = if (totalAnswers == 0) 0f else correctAnswers.toFloat() / totalAnswers
} 