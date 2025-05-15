package com.mastermind.wordwise.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.usecase.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()
    
    fun updateName(name: String) {
        _uiState.update { it.copy(
            name = name,
            nameError = validateName(name)
        )}
    }
    
    fun selectLevel(level: LanguageLevel) {
        _uiState.update { it.copy(selectedLevel = level) }
    }
    
    fun createUser(onComplete: (Boolean) -> Unit) {
        val currentState = uiState.value
        val nameValidationError = validateName(currentState.name)
        
        if (nameValidationError != null) {
            _uiState.update { it.copy(nameError = nameValidationError) }
            onComplete(false)
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                createUserUseCase(
                    name = currentState.name,
                    level = currentState.selectedLevel
                )
                _uiState.update { it.copy(isCompleted = true, isLoading = false) }
                onComplete(true)
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                )}
                onComplete(false)
            }
        }
    }
    
    private fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "Имя не может быть пустым"
            name.length < 2 -> "Имя должно содержать минимум 2 символа"
            else -> null
        }
    }
}

data class OnboardingUiState(
    val name: String = "",
    val nameError: String? = null,
    val selectedLevel: LanguageLevel = LanguageLevel.BEGINNER,
    val isLoading: Boolean = false,
    val isCompleted: Boolean = false,
    val error: String? = null
) 