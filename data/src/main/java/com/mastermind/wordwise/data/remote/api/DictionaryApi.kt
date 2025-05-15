package com.mastermind.wordwise.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface for the Free Dictionary API
 * https://dictionaryapi.dev/
 */
interface DictionaryApi {
    
    @GET("api/v2/entries/en/{word}")
    suspend fun getWordDefinition(@Path("word") word: String): List<WordResponse>
}

/**
 * Response model for the Dictionary API
 */
data class WordResponse(
    val word: String,
    val phonetic: String? = null,
    val phonetics: List<PhoneticResponse> = emptyList(),
    val meanings: List<MeaningResponse> = emptyList(),
    val origin: String? = null
)

data class PhoneticResponse(
    val text: String? = null,
    val audio: String? = null
)

data class MeaningResponse(
    val partOfSpeech: String,
    val definitions: List<DefinitionResponse> = emptyList()
)

data class DefinitionResponse(
    val definition: String,
    val example: String? = null,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
) 