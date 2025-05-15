package com.mastermind.wordwise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.model.Word

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey
    val id: String,
    val russianWord: String,
    val englishWord: String,
    val level: String,
    val isLearned: Boolean,
    val needsRepetition: Boolean
)

fun WordEntity.toDomain(): Word {
    return Word(
        id = id,
        russianWord = russianWord,
        englishWord = englishWord,
        level = when (level) {
            "BEGINNER" -> LanguageLevel.BEGINNER
            "INTERMEDIATE" -> LanguageLevel.INTERMEDIATE
            "ADVANCED" -> LanguageLevel.ADVANCED
            else -> LanguageLevel.BEGINNER
        },
        isLearned = isLearned,
        needsRepetition = needsRepetition
    )
}

fun Word.toEntity(): WordEntity {
    return WordEntity(
        id = id,
        russianWord = russianWord,
        englishWord = englishWord,
        level = level.name,
        isLearned = isLearned,
        needsRepetition = needsRepetition
    )
} 