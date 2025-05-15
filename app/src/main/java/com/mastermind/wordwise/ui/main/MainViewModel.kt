package com.mastermind.wordwise.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.wordwise.domain.model.User
import com.mastermind.wordwise.domain.usecase.GetLearnedWordsUseCase
import com.mastermind.wordwise.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val user: User? = null,
    val isLoading: Boolean = true,
    val learnedWordsCount: Int = 0,
    val wordsToReviewCount: Int = 0,
    val currentStreak: Int = 0,
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getLearnedWordsUseCase: GetLearnedWordsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                // Комбинируем потоки данных пользователя и изученных слов
                combine(
                    getUserUseCase(),
                    getLearnedWordsUseCase()
                ) { user, learnedWords ->
                    val wordsToReview = learnedWords.count { it.needsRepetition }
                    
                    _uiState.update { state ->
                        state.copy(
                            user = user,
                            isLoading = false,
                            learnedWordsCount = learnedWords.size,
                            wordsToReviewCount = wordsToReview,
                            // В будущем можно будет добавить поддержку streaks
                            currentStreak = calculateCurrentStreak(),
                            error = null
                        )
                    }
                }.collect {}
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка загрузки данных: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
    
    // Функция для подсчета текущей серии ежедневных занятий
    // (в будущей реализации можно хранить историю занятий)
    private fun calculateCurrentStreak(): Int {
        // Временная заглушка
        return 1
    }
} 