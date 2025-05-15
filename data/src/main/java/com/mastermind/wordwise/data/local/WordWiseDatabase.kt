package com.mastermind.wordwise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mastermind.wordwise.data.local.dao.TestResultDao
import com.mastermind.wordwise.data.local.dao.WordDao
import com.mastermind.wordwise.data.local.entity.TestResultEntity
import com.mastermind.wordwise.data.local.entity.WordEntity

@Database(
    entities = [WordEntity::class, TestResultEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WordWiseDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun testResultDao(): TestResultDao
    
    companion object {
        const val DATABASE_NAME = "wordwise_db"
    }
} 