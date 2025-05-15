package com.mastermind.wordwise.data.repository

import android.util.Log
import com.mastermind.wordwise.data.local.dao.WordDao
import com.mastermind.wordwise.data.local.entity.toDomain
import com.mastermind.wordwise.data.local.entity.toEntity
import com.mastermind.wordwise.data.remote.api.DictionaryApi
import com.mastermind.wordwise.data.remote.api.RandomWordApi
import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.model.Word
import com.mastermind.wordwise.domain.repository.WordRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val wordDao: WordDao,
    private val randomWordApi: RandomWordApi,
    private val dictionaryApi: DictionaryApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WordRepository {
    override suspend fun getWordsByLevel(level: LanguageLevel): Flow<List<Word>> {
        return wordDao.getWordsByLevel(level.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getLearnedWords(): Flow<List<Word>> {
        return wordDao.getLearnedWords().map { entities ->
            Log.d("WordRepository", "Retrieved ${entities.size} learned words")
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getWordsForTest(level: LanguageLevel, count: Int): List<Word> {
        // Проверяем, есть ли достаточно слов в базе данных
        val existingWords = wordDao.getRandomWordsByLevel(level.name, count)
        
        if (existingWords.size >= count) {
            return existingWords.map { it.toDomain() }
        }
        
        // Если недостаточно слов, получаем новые случайные слова из API
        // и сохраняем их в базу данных
        return withContext(ioDispatcher) {
            try {
                val newWords = fetchNewRandomWords(level, count - existingWords.size)
                
                // Если не удалось получить слова из API, используем предварительно заполненный список
                val finalWords = if (newWords.isEmpty()) {
                    getDefaultWords(level, count - existingWords.size)
                } else {
                    newWords
                }
                
                wordDao.insertWords(finalWords.map { it.toEntity() })
                
                // Возвращаем комбинацию существующих и новых слов
                val allWords = existingWords.map { it.toDomain() } + finalWords
                allWords.shuffled().take(count)
            } catch (e: Exception) {
                // В случае ошибки, используем предварительно заполненный список
                val defaultWords = getDefaultWords(level, count - existingWords.size)
                wordDao.insertWords(defaultWords.map { it.toEntity() })
                
                val allWords = existingWords.map { it.toDomain() } + defaultWords
                allWords.shuffled().take(count)
            }
        }
    }

    override suspend fun markWordAsLearned(wordId: String) {
        try {
            wordDao.markWordAsLearned(wordId)
            Log.d("WordRepository", "Word marked as learned: $wordId")
        } catch (e: Exception) {
            Log.e("WordRepository", "Failed to mark word as learned: $wordId", e)
            throw e
        }
    }

    override suspend fun markWordForRepetition(wordId: String, needsRepetition: Boolean) {
        wordDao.markWordForRepetition(wordId, needsRepetition)
    }

    override suspend fun resetAllWords() {
        wordDao.resetAllWords()
    }
    
    private suspend fun fetchNewRandomWords(level: LanguageLevel, count: Int): List<Word> {
        val words = mutableListOf<Word>()
        
        repeat(count) {
            try {
                val response = when (level) {
                    LanguageLevel.BEGINNER -> randomWordApi.getRandomNoun()
                    LanguageLevel.INTERMEDIATE -> randomWordApi.getRandomVerb()
                    LanguageLevel.ADVANCED -> randomWordApi.getRandomAdjective()
                }
                
                if (response.isNotEmpty()) {
                    val randomWordData = response.first()
                    
                    // Получаем перевод слова на русский язык
                    val russianTranslation = translateToRussian(randomWordData.word)
                    
                    // Пытаемся получить дополнительную информацию из Dictionary API
                    try {
                        val dictionaryData = dictionaryApi.getWordDefinition(randomWordData.word)
                        if (dictionaryData.isNotEmpty()) {
                            val firstResult = dictionaryData.first()
                            
                            // Создаем слово с данными из обоих API и переводом
                            words.add(
                                Word(
                                    id = UUID.randomUUID().toString(),
                                    russianWord = russianTranslation,
                                    englishWord = randomWordData.word,
                                    level = level,
                                    isLearned = false,
                                    needsRepetition = false
                                )
                            )
                        }
                    } catch (e: Exception) {
                        // Если не удалось получить данные из Dictionary API, используем только данные из Random API
                        words.add(
                            Word(
                                id = UUID.randomUUID().toString(),
                                russianWord = russianTranslation,
                                englishWord = randomWordData.word,
                                level = level,
                                isLearned = false,
                                needsRepetition = false
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                // Пропускаем ошибку и продолжаем попытки
            }
        }
        
        return words
    }
    
    /**
     * Переводит слово с английского на русский язык
     * В реальном приложении здесь должен быть вызов API перевода
     */
    private fun translateToRussian(word: String): String {
        // Простой словарь для демонстрации
        val translations = mapOf(
            // Существительные (Beginner)
            "apple" to "яблоко",
            "book" to "книга",
            "car" to "машина",
            "house" to "дом",
            "dog" to "собака",
            "cat" to "кошка",
            "table" to "стол",
            "chair" to "стул",
            "phone" to "телефон",
            "computer" to "компьютер",
            "water" to "вода",
            "food" to "еда",
            "friend" to "друг",
            "family" to "семья",
            "school" to "школа",
            "work" to "работа",
            "time" to "время",
            "day" to "день",
            "night" to "ночь",
            "year" to "год",
            
            // Глаголы (Intermediate)
            "run" to "бежать",
            "walk" to "ходить",
            "talk" to "говорить",
            "eat" to "есть",
            "drink" to "пить",
            "sleep" to "спать",
            "work" to "работать",
            "study" to "учиться",
            "read" to "читать",
            "write" to "писать",
            "listen" to "слушать",
            "watch" to "смотреть",
            "play" to "играть",
            "help" to "помогать",
            "think" to "думать",
            "know" to "знать",
            "understand" to "понимать",
            "feel" to "чувствовать",
            "see" to "видеть",
            "hear" to "слышать",
            
            // Прилагательные (Advanced)
            "good" to "хороший",
            "bad" to "плохой",
            "big" to "большой",
            "small" to "маленький",
            "happy" to "счастливый",
            "sad" to "грустный",
            "beautiful" to "красивый",
            "ugly" to "некрасивый",
            "fast" to "быстрый",
            "slow" to "медленный",
            "hot" to "горячий",
            "cold" to "холодный",
            "new" to "новый",
            "old" to "старый",
            "easy" to "легкий",
            "difficult" to "трудный",
            "expensive" to "дорогой",
            "cheap" to "дешевый",
            "interesting" to "интересный",
            "boring" to "скучный"
        )
        
        // Возвращаем перевод или оригинальное слово, если перевод не найден
        return translations[word.lowercase()] ?: "${word} (перевод отсутствует)"
    }
    
    /**
     * Возвращает предварительно заполненный список слов для указанного уровня языка
     */
    private fun getDefaultWords(level: LanguageLevel, count: Int): List<Word> {
        val words = when (level) {
            LanguageLevel.BEGINNER -> listOf(
                Word(UUID.randomUUID().toString(), "яблоко", "apple", level, false, false),
                Word(UUID.randomUUID().toString(), "книга", "book", level, false, false),
                Word(UUID.randomUUID().toString(), "машина", "car", level, false, false),
                Word(UUID.randomUUID().toString(), "дом", "house", level, false, false),
                Word(UUID.randomUUID().toString(), "собака", "dog", level, false, false),
                Word(UUID.randomUUID().toString(), "кошка", "cat", level, false, false),
                Word(UUID.randomUUID().toString(), "стол", "table", level, false, false),
                Word(UUID.randomUUID().toString(), "стул", "chair", level, false, false),
                Word(UUID.randomUUID().toString(), "телефон", "phone", level, false, false),
                Word(UUID.randomUUID().toString(), "компьютер", "computer", level, false, false),
                Word(UUID.randomUUID().toString(), "вода", "water", level, false, false),
                Word(UUID.randomUUID().toString(), "еда", "food", level, false, false),
                Word(UUID.randomUUID().toString(), "друг", "friend", level, false, false),
                Word(UUID.randomUUID().toString(), "семья", "family", level, false, false),
                Word(UUID.randomUUID().toString(), "школа", "school", level, false, false)
            )
            LanguageLevel.INTERMEDIATE -> listOf(
                Word(UUID.randomUUID().toString(), "бежать", "run", level, false, false),
                Word(UUID.randomUUID().toString(), "ходить", "walk", level, false, false),
                Word(UUID.randomUUID().toString(), "говорить", "talk", level, false, false),
                Word(UUID.randomUUID().toString(), "есть", "eat", level, false, false),
                Word(UUID.randomUUID().toString(), "пить", "drink", level, false, false),
                Word(UUID.randomUUID().toString(), "спать", "sleep", level, false, false),
                Word(UUID.randomUUID().toString(), "работать", "work", level, false, false),
                Word(UUID.randomUUID().toString(), "учиться", "study", level, false, false),
                Word(UUID.randomUUID().toString(), "читать", "read", level, false, false),
                Word(UUID.randomUUID().toString(), "писать", "write", level, false, false),
                Word(UUID.randomUUID().toString(), "слушать", "listen", level, false, false),
                Word(UUID.randomUUID().toString(), "смотреть", "watch", level, false, false),
                Word(UUID.randomUUID().toString(), "играть", "play", level, false, false),
                Word(UUID.randomUUID().toString(), "помогать", "help", level, false, false),
                Word(UUID.randomUUID().toString(), "думать", "think", level, false, false)
            )
            LanguageLevel.ADVANCED -> listOf(
                Word(UUID.randomUUID().toString(), "хороший", "good", level, false, false),
                Word(UUID.randomUUID().toString(), "плохой", "bad", level, false, false),
                Word(UUID.randomUUID().toString(), "большой", "big", level, false, false),
                Word(UUID.randomUUID().toString(), "маленький", "small", level, false, false),
                Word(UUID.randomUUID().toString(), "счастливый", "happy", level, false, false),
                Word(UUID.randomUUID().toString(), "грустный", "sad", level, false, false),
                Word(UUID.randomUUID().toString(), "красивый", "beautiful", level, false, false),
                Word(UUID.randomUUID().toString(), "некрасивый", "ugly", level, false, false),
                Word(UUID.randomUUID().toString(), "быстрый", "fast", level, false, false),
                Word(UUID.randomUUID().toString(), "медленный", "slow", level, false, false),
                Word(UUID.randomUUID().toString(), "горячий", "hot", level, false, false),
                Word(UUID.randomUUID().toString(), "холодный", "cold", level, false, false),
                Word(UUID.randomUUID().toString(), "новый", "new", level, false, false),
                Word(UUID.randomUUID().toString(), "старый", "old", level, false, false),
                Word(UUID.randomUUID().toString(), "легкий", "easy", level, false, false)
            )
        }
        
        // Возвращаем случайные слова из списка, не больше чем count
        return words.shuffled().take(count.coerceAtMost(words.size))
    }
}