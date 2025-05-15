package com.mastermind.wordwise.di

import android.content.Context
import androidx.room.Room
import com.mastermind.wordwise.data.local.WordWiseDatabase
import com.mastermind.wordwise.data.local.dao.TestResultDao
import com.mastermind.wordwise.data.local.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WordWiseDatabase {
        return Room.databaseBuilder(
            context,
            WordWiseDatabase::class.java,
            WordWiseDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideWordDao(database: WordWiseDatabase): WordDao {
        return database.wordDao()
    }
    
    @Provides
    @Singleton
    fun provideTestResultDao(database: WordWiseDatabase): TestResultDao {
        return database.testResultDao()
    }
} 