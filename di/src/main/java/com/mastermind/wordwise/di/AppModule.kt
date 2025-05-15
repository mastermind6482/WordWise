package com.mastermind.wordwise.di

import android.content.Context
import com.mastermind.wordwise.data.local.UserPreferences
import com.mastermind.wordwise.data.util.WordsInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
    
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
    
    @Provides
    @Singleton
    fun provideWordsInitializer(
        initializer: WordsInitializer,
        applicationScope: CoroutineScope
    ): WordsInitializer {
        initializer.initialize(applicationScope)
        return initializer
    }
} 