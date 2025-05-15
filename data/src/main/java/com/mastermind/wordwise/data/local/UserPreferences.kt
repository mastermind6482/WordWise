package com.mastermind.wordwise.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    private val context: Context
) {
    companion object {
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_LEVEL = stringPreferencesKey("user_level")
        private val WORDS_LEARNED = intPreferencesKey("words_learned")
        private val CORRECT_ANSWERS = intPreferencesKey("correct_answers")
        private val TOTAL_ANSWERS = intPreferencesKey("total_answers")
        private val DARK_MODE = stringPreferencesKey("dark_mode")
    }
    
    val userFlow: Flow<User?> = context.dataStore.data.map { preferences ->
        val name = preferences[USER_NAME] ?: return@map null
        val levelStr = preferences[USER_LEVEL] ?: LanguageLevel.BEGINNER.name
        
        User(
            name = name,
            level = when (levelStr) {
                LanguageLevel.BEGINNER.name -> LanguageLevel.BEGINNER
                LanguageLevel.INTERMEDIATE.name -> LanguageLevel.INTERMEDIATE
                LanguageLevel.ADVANCED.name -> LanguageLevel.ADVANCED
                else -> LanguageLevel.BEGINNER
            },
            wordsLearned = preferences[WORDS_LEARNED] ?: 0,
            correctAnswers = preferences[CORRECT_ANSWERS] ?: 0,
            totalAnswers = preferences[TOTAL_ANSWERS] ?: 0
        )
    }
    
    val themeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: "system"
    }
    
    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_LEVEL] = user.level.name
            preferences[WORDS_LEARNED] = user.wordsLearned
            preferences[CORRECT_ANSWERS] = user.correctAnswers
            preferences[TOTAL_ANSWERS] = user.totalAnswers
        }
    }
    
    suspend fun updateUserLevel(level: LanguageLevel) {
        context.dataStore.edit { preferences ->
            preferences[USER_LEVEL] = level.name
        }
    }
    
    suspend fun updateUserStats(wordsLearned: Int = 0, correctAnswers: Int = 0, totalAnswers: Int = 0) {
        context.dataStore.edit { preferences ->
            val currentWordsLearned = preferences[WORDS_LEARNED] ?: 0
            val currentCorrectAnswers = preferences[CORRECT_ANSWERS] ?: 0
            val currentTotalAnswers = preferences[TOTAL_ANSWERS] ?: 0
            
            preferences[WORDS_LEARNED] = currentWordsLearned + wordsLearned
            preferences[CORRECT_ANSWERS] = currentCorrectAnswers + correctAnswers
            preferences[TOTAL_ANSWERS] = currentTotalAnswers + totalAnswers
        }
    }
    
    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = theme
        }
    }
    
    suspend fun resetProgress() {
        context.dataStore.edit { preferences ->
            preferences[WORDS_LEARNED] = 0
            preferences[CORRECT_ANSWERS] = 0
            preferences[TOTAL_ANSWERS] = 0
        }
    }
} 