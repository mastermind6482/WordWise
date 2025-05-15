package com.mastermind.wordwise.data.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mastermind.wordwise.data.local.dao.WordDao
import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.model.Word
import com.mastermind.wordwise.data.local.entity.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class WordsInitializer @Inject constructor(
    private val context: Context,
    private val wordDao: WordDao
) {
    fun initialize(coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            // Check if words are already loaded
            val existingWords = wordDao.getWordsByLevel(LanguageLevel.BEGINNER.name).first()
            if (existingWords.isEmpty()) {
                val beginnerWords = getDefaultBeginnerWords()
                val intermediateWords = getDefaultIntermediateWords()
                val advancedWords = getDefaultAdvancedWords()
                
                val allWords = beginnerWords + intermediateWords + advancedWords
                wordDao.insertWords(allWords.map { it.toEntity() })
            }
        }
    }
    
    private fun getDefaultBeginnerWords(): List<Word> {
        return listOf(
            Word(id = UUID.randomUUID().toString(), russianWord = "Привет", englishWord = "Hello", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Пока", englishWord = "Goodbye", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Да", englishWord = "Yes", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Нет", englishWord = "No", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Спасибо", englishWord = "Thank you", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Пожалуйста", englishWord = "Please", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Извините", englishWord = "Sorry", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Дом", englishWord = "House", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Кошка", englishWord = "Cat", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Собака", englishWord = "Dog", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Вода", englishWord = "Water", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Еда", englishWord = "Food", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Хорошо", englishWord = "Good", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Плохо", englishWord = "Bad", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Большой", englishWord = "Big", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Маленький", englishWord = "Small", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Книга", englishWord = "Book", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Телефон", englishWord = "Phone", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "Время", englishWord = "Time", level = LanguageLevel.BEGINNER),
            Word(id = UUID.randomUUID().toString(), russianWord = "День", englishWord = "Day", level = LanguageLevel.BEGINNER)
        )
    }
    
    private fun getDefaultIntermediateWords(): List<Word> {
        return listOf(
            Word(id = UUID.randomUUID().toString(), russianWord = "Образование", englishWord = "Education", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Опыт", englishWord = "Experience", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Развитие", englishWord = "Development", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Технология", englishWord = "Technology", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Путешествие", englishWord = "Travel", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Встреча", englishWord = "Meeting", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Проект", englishWord = "Project", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Важный", englishWord = "Important", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Решение", englishWord = "Decision", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Возможность", englishWord = "Opportunity", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Успех", englishWord = "Success", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Разный", englishWord = "Different", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Сложный", englishWord = "Difficult", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Простой", englishWord = "Simple", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Интересный", englishWord = "Interesting", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Будущее", englishWord = "Future", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Прошлое", englishWord = "Past", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Настоящее", englishWord = "Present", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Цель", englishWord = "Goal", level = LanguageLevel.INTERMEDIATE),
            Word(id = UUID.randomUUID().toString(), russianWord = "Достижение", englishWord = "Achievement", level = LanguageLevel.INTERMEDIATE)
        )
    }
    
    private fun getDefaultAdvancedWords(): List<Word> {
        return listOf(
            Word(id = UUID.randomUUID().toString(), russianWord = "Осведомленность", englishWord = "Awareness", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Благоприятствовать", englishWord = "Facilitate", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Воплощение", englishWord = "Embodiment", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Противоречие", englishWord = "Contradiction", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Последовательность", englishWord = "Consistency", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Обстоятельства", englishWord = "Circumstances", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Взаимодействие", englishWord = "Interaction", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Непредвиденный", englishWord = "Unforeseen", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Двусмысленность", englishWord = "Ambiguity", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Предрасположенность", englishWord = "Predisposition", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Интерпретация", englishWord = "Interpretation", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Универсальный", englishWord = "Universal", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Философия", englishWord = "Philosophy", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Многогранный", englishWord = "Multifaceted", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Концептуальный", englishWord = "Conceptual", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Существенный", englishWord = "Substantial", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Недопонимание", englishWord = "Misunderstanding", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Олицетворение", englishWord = "Personification", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Представление", englishWord = "Representation", level = LanguageLevel.ADVANCED),
            Word(id = UUID.randomUUID().toString(), russianWord = "Преобразование", englishWord = "Transformation", level = LanguageLevel.ADVANCED)
        )
    }
} 