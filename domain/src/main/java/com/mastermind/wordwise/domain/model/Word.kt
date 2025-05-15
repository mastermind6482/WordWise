package com.mastermind.wordwise.domain.model

data class Word(
    val id: String,
    val russianWord: String,
    val englishWord: String,
    val level: LanguageLevel,
    val isLearned: Boolean = false,
    val needsRepetition: Boolean = false
) 