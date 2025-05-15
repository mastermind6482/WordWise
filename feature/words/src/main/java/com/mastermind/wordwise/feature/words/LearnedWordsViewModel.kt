package com.mastermind.wordwise.feature.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.wordwise.domain.model.Word
import com.mastermind.wordwise.domain.usecase.GetLearnedWordsUseCase
import com.mastermind.wordwise.domain.usecase.MarkWordForRepetitionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnedWordsViewModel @Inject constructor(
    private val getLearnedWordsUseCase: GetLearnedWordsUseCase,
    private val markWordForRepetitionUseCase: MarkWordForRepetitionUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LearnedWordsUiState())
    val uiState: StateFlow<LearnedWordsUiState> = _uiState.asStateFlow()
    
    init {
        loadLearnedWords()
    }
    
    private fun loadLearnedWords() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                getLearnedWordsUseCase().collect { words ->
                    _uiState.update { 
                        it.copy(
                            words = words,
                            filteredWords = words,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Unknown error",
                        isLoading = false
                    )
                }
            }
        }
    }
    
    fun toggleWordRepetition(wordId: String, needsRepetition: Boolean) {
        viewModelScope.launch {
            try {
                markWordForRepetitionUseCase(wordId, needsRepetition)
                
                // Update local state immediately for better UX
                _uiState.update { state ->
                    val updatedWords = state.words.map { word ->
                        if (word.id == wordId) {
                            word.copy(needsRepetition = needsRepetition)
                        } else {
                            word
                        }
                    }
                    
                    state.copy(
                        words = updatedWords,
                        filteredWords = filterWords(updatedWords, state.searchQuery)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to update word")
                }
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredWords = filterWords(state.words, query)
            )
        }
    }
    
    private fun filterWords(words: List<Word>, query: String): List<Word> {
        if (query.isBlank()) return words
        
        val lowercaseQuery = query.lowercase()
        return words.filter { word ->
            word.russianWord.lowercase().contains(lowercaseQuery) ||
            word.englishWord.lowercase().contains(lowercaseQuery)
        }
    }
}

data class LearnedWordsUiState(
    val words: List<Word> = emptyList(),
    val filteredWords: List<Word> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) 