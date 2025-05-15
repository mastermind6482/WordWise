package com.mastermind.wordwise.feature.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.wordwise.domain.model.Word
import com.mastermind.wordwise.domain.usecase.GetUserUseCase
import com.mastermind.wordwise.domain.usecase.GetWordsForTestUseCase
import com.mastermind.wordwise.domain.usecase.SaveTestResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TestViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getWordsForTestUseCase: GetWordsForTestUseCase,
    private val saveTestResultUseCase: SaveTestResultUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()
    
    private val incorrectWords = mutableListOf<Word>()
    
    init {
        loadTest()
    }
    
    private fun loadTest() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                // Get user to determine level
                val user = getUserUseCase().first()
                if (user == null) {
                    _uiState.update {
                        it.copy(
                            error = "Пользователь не найден",
                            isLoading = false
                        )
                    }
                    return@launch
                }
                
                // Get words for test
                val words = getWordsForTestUseCase(user.level, count = 10)
                if (words.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            error = "Нет доступных слов для теста",
                            isLoading = false
                        )
                    }
                    return@launch
                }
                
                // Initialize the test
                val currentWord = words.first()
                val options = generateOptions(currentWord, words)
                
                _uiState.update {
                    it.copy(
                        words = words,
                        currentWordIndex = 0,
                        currentWord = currentWord,
                        options = options,
                        isLoading = false,
                        selectedOptionIndex = null,
                        result = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Неизвестная ошибка",
                        isLoading = false
                    )
                }
            }
        }
    }
    
    fun selectOption(index: Int) {
        _uiState.update { it.copy(selectedOptionIndex = index) }
    }
    
    fun checkAnswer() {
        val currentState = uiState.value
        val selectedOption = currentState.selectedOptionIndex ?: return
        val currentWord = currentState.currentWord ?: return
        
        val selectedOptionText = currentState.options[selectedOption]
        val isCorrect = selectedOptionText == currentWord.englishWord
        
        if (!isCorrect) {
            incorrectWords.add(currentWord)
        }
        
        _uiState.update {
            it.copy(
                result = isCorrect,
                correctAnswersCount = it.correctAnswersCount + if (isCorrect) 1 else 0
            )
        }
        
        // Auto proceed to next word after delay
        viewModelScope.launch {
            delay(1500) // Give user time to see the result
            nextWord()
        }
    }
    
    fun nextWord() {
        val currentState = uiState.value
        val nextIndex = currentState.currentWordIndex + 1
        
        if (nextIndex >= currentState.words.size) {
            // Test is completed
            completeTest()
            return
        }
        
        val nextWord = currentState.words[nextIndex]
        val options = generateOptions(nextWord, currentState.words)
        
        _uiState.update {
            it.copy(
                currentWordIndex = nextIndex,
                currentWord = nextWord,
                options = options,
                selectedOptionIndex = null,
                result = null
            )
        }
    }
    
    private fun completeTest() {
        val currentState = uiState.value
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                saveTestResultUseCase(
                    correctAnswers = currentState.correctAnswersCount,
                    totalQuestions = currentState.words.size,
                    incorrectWords = incorrectWords,
                    allWords = currentState.words
                )
                _uiState.update {
                    it.copy(
                        isTestCompleted = true,
                        isLoading = false,
                        incorrectWords = incorrectWords
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Неизвестная ошибка при сохранении результатов",
                        isLoading = false
                    )
                }
            }
        }
    }
    
    fun restartTest() {
        incorrectWords.clear()
        _uiState.update { 
            it.copy(
                currentWordIndex = 0,
                selectedOptionIndex = null,
                result = null,
                correctAnswersCount = 0,
                isTestCompleted = false
            )
        }
        loadTest()
    }
    
    private fun generateOptions(currentWord: Word, allWords: List<Word>): List<String> {
        val correctOption = currentWord.englishWord
        val otherOptions = allWords
            .filter { it.id != currentWord.id }
            .map { it.englishWord }
            .shuffled()
            .take(3)
        
        return (otherOptions + correctOption).shuffled()
    }
}

data class TestUiState(
    val words: List<Word> = emptyList(),
    val currentWordIndex: Int = 0,
    val currentWord: Word? = null,
    val options: List<String> = emptyList(),
    val selectedOptionIndex: Int? = null,
    val result: Boolean? = null,
    val correctAnswersCount: Int = 0,
    val isLoading: Boolean = false,
    val isTestCompleted: Boolean = false,
    val incorrectWords: List<Word> = emptyList(),
    val error: String? = null
)