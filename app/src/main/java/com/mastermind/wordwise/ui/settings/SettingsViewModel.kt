package com.mastermind.wordwise.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.wordwise.domain.model.LanguageLevel
import com.mastermind.wordwise.domain.usecase.GetUserUseCase
import com.mastermind.wordwise.domain.usecase.ResetProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val resetProgressUseCase: ResetProgressUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadUserSettings()
    }
    
    private fun loadUserSettings() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                getUserUseCase().collect { user ->
                    _uiState.update { 
                        it.copy(
                            userName = user?.name ?: "",
                            selectedLevel = user?.level ?: LanguageLevel.BEGINNER,
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
    
    fun resetProgress(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                resetProgressUseCase()
                
                _uiState.update { it.copy(isLoading = false, showResetConfirmation = false) }
                onComplete(true)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "Failed to reset progress",
                        isLoading = false,
                        showResetConfirmation = false
                    )
                }
                onComplete(false)
            }
        }
    }
    
    fun showResetConfirmation() {
        _uiState.update { it.copy(showResetConfirmation = true) }
    }
    
    fun hideResetConfirmation() {
        _uiState.update { it.copy(showResetConfirmation = false) }
    }
    
    fun toggleTheme(isDarkMode: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDarkMode) }
        // In a real app, save this preference to DataStore
    }
}

data class SettingsUiState(
    val userName: String = "",
    val selectedLevel: LanguageLevel = LanguageLevel.BEGINNER,
    val isDarkMode: Boolean = false,
    val isLoading: Boolean = false,
    val showResetConfirmation: Boolean = false,
    val error: String? = null
) 