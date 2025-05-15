package com.mastermind.wordwise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mastermind.wordwise.data.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)
    
    @Query("SELECT * FROM words WHERE level = :level")
    fun getWordsByLevel(level: String): Flow<List<WordEntity>>
    
    @Query("SELECT * FROM words WHERE isLearned = 1")
    fun getLearnedWords(): Flow<List<WordEntity>>
    
    @Query("SELECT * FROM words WHERE level = :level AND isLearned = 0 ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomWordsByLevel(level: String, count: Int): List<WordEntity>
    
    @Query("UPDATE words SET isLearned = 1 WHERE id = :wordId")
    suspend fun markWordAsLearned(wordId: String)
    
    @Query("UPDATE words SET needsRepetition = :needsRepetition WHERE id = :wordId")
    suspend fun markWordForRepetition(wordId: String, needsRepetition: Boolean)
    
    @Query("UPDATE words SET isLearned = 0, needsRepetition = 0")
    suspend fun resetAllWords()
    
    @Query("SELECT * FROM words WHERE id IN (:wordIds)")
    suspend fun getWordsByIds(wordIds: List<String>): List<WordEntity>
} 