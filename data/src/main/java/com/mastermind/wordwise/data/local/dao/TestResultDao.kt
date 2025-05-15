package com.mastermind.wordwise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mastermind.wordwise.data.local.entity.TestResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(testResult: TestResultEntity)
    
    @Query("SELECT * FROM test_results ORDER BY timestamp DESC")
    fun getTestResults(): Flow<List<TestResultEntity>>
} 