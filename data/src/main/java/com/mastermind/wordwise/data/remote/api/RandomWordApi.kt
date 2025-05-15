package com.mastermind.wordwise.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the Random Word API
 * https://random-words-api.vercel.app/
 */
interface RandomWordApi {
    
    @GET("word")
    suspend fun getRandomWord(): List<RandomWordResponse>
    
    @GET("word/english/noun")
    suspend fun getRandomNoun(): List<RandomWordResponse>
    
    @GET("word/english/verb")
    suspend fun getRandomVerb(): List<RandomWordResponse>
    
    @GET("word/english/adjective")
    suspend fun getRandomAdjective(): List<RandomWordResponse>
}

/**
 * Response model for the Random Word API
 */
data class RandomWordResponse(
    val word: String,
    val definition: String,
    val pronunciation: String? = null
) 