package com.mastermind.wordwise.feature.test

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mastermind.wordwise.domain.model.Word
import com.mastermind.wordwise.ui.components.GradientButton
import com.mastermind.wordwise.ui.components.GradientCard
import com.mastermind.wordwise.ui.components.PrimaryButton
import com.mastermind.wordwise.ui.components.SecondaryButton
import com.mastermind.wordwise.ui.components.TestOptionCard
import com.mastermind.wordwise.ui.components.WordCard
import com.mastermind.wordwise.ui.theme.GradientColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    viewModel: TestViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Анимация для постепенного отображения элементов экрана
    var showContent by remember { mutableStateOf(false) }
    
    LaunchedEffect(key1 = true) {
        delay(100)
        showContent = true
    }
    
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (uiState.isTestCompleted) "Результаты теста" else "Тест",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
            if (uiState.isLoading) {
                LoadingIndicator()
            } else if (uiState.isTestCompleted) {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    exit = fadeOut()
                ) {
                    TestResultsScreen(
                        correctAnswers = uiState.correctAnswersCount,
                        totalQuestions = uiState.words.size,
                        incorrectWords = uiState.incorrectWords,
                        onRestart = viewModel::restartTest,
                        onNavigateBack = onNavigateBack
                    )
                }
            } else {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    exit = fadeOut()
                ) {
                    TestContentScreen(
                        currentWordIndex = uiState.currentWordIndex,
                        totalWords = uiState.words.size,
                        russianWord = uiState.currentWord?.russianWord ?: "",
                        options = uiState.options,
                        selectedOptionIndex = uiState.selectedOptionIndex,
                        result = uiState.result,
                        correctAnswersCount = uiState.correctAnswersCount,
                        onOptionSelected = viewModel::selectOption,
                        onCheckAnswer = viewModel::checkAnswer,
                        currentWord = uiState.currentWord
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(60.dp),
            strokeWidth = 5.dp
        )
    }
}

@Composable
fun TestContentScreen(
    currentWordIndex: Int,
    totalWords: Int,
    russianWord: String,
    options: List<String>,
    selectedOptionIndex: Int?,
    result: Boolean?,
    correctAnswersCount: Int,
    onOptionSelected: (Int) -> Unit,
    onCheckAnswer: () -> Unit,
    currentWord: Word? = null
) {
    val progressAnimated by animateFloatAsState(
        targetValue = (currentWordIndex + 1).toFloat() / totalWords,
        animationSpec = tween(500),
        label = "Progress Animation"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress indicator
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Слово ${currentWordIndex + 1} из $totalWords",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Правильных: $correctAnswersCount",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progressAnimated },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Russian word
        GradientCard(
            gradient = GradientColors.PurpleBlueGradient,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Переведите слово:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = russianWord,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Options
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            options.forEachIndexed { index, option ->
                TestOptionCard(
                    text = option,
                    isSelected = selectedOptionIndex == index,
                    isCorrect = when {
                        result == null -> null // Not showing result yet
                        result && selectedOptionIndex == index -> true // Correct answer
                        !result && selectedOptionIndex == index -> false // Wrong answer 
                        !result && currentWord?.englishWord == option -> true // Show correct answer
                        else -> null // Other options, don't show result
                    },
                    onClick = { onOptionSelected(index) }
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Check answer button
        GradientButton(
            text = "Проверить",
            onClick = onCheckAnswer,
            enabled = selectedOptionIndex != null && result == null,
            gradient = GradientColors.GreenTealGradient
        )
    }
}

@Composable
fun TestResultsScreen(
    correctAnswers: Int,
    totalQuestions: Int,
    incorrectWords: List<Word>,
    onRestart: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val percentage = (correctAnswers.toFloat() / totalQuestions) * 100
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Results summary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            com.mastermind.wordwise.ui.components.CircularProgressIndicator(
                progress = correctAnswers.toFloat() / totalQuestions,
                size = 150f,
                centerText = "${percentage.toInt()}%",
                progressColor = if (percentage >= 70) MaterialTheme.colorScheme.tertiary
                                else MaterialTheme.colorScheme.primary
            )
        }
        
        // Congratulation or suggestion text
        GradientCard(
            gradient = if (percentage >= 70) GradientColors.GreenTealGradient else GradientColors.PurpleBlueGradient,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Правильных ответов: $correctAnswers из $totalQuestions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = if (percentage >= 70) 
                            "Отличный результат! Продолжайте в том же духе!"
                          else 
                            "Старайтесь больше практиковаться для лучших результатов.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Words that need repetition
        if (incorrectWords.isNotEmpty()) {
            Text(
                text = "Слова для повторения:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Start
            )
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(incorrectWords) { word ->
                    WordCard(
                        word = word,
                        onToggleRepetition = { }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SecondaryButton(
                text = "На главную",
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f)
            )
            
            PrimaryButton(
                text = "Начать заново",
                onClick = onRestart,
                modifier = Modifier.weight(1f)
            )
        }
    }
} 