package com.mastermind.wordwise.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Основная цветовая палитра
val PastelBlue = Color(0xFF8AADED)
val LightPastelBlue = Color(0xFFBBCCFF)
val LighterPastelBlue = Color(0xFFDDE4FF)
val DarkBlue = Color(0xFF0A2463)

// Пастельно-розовый
val PastelPink = Color(0xFFFFB6C1)
val LightPastelPink = Color(0xFFFFD1D9)
val LighterPastelPink = Color(0xFFFFE8EC)
val DarkPink = Color(0xFF99546A)

// Пастельно-зеленый
val PastelGreen = Color(0xFF98D8AA)
val LightPastelGreen = Color(0xFFBEEBCF)
val LighterPastelGreen = Color(0xFFDFFAE9)
val DarkGreen = Color(0xFF1E5631)

// Пастельно-красный
val PastelRed = Color(0xFFFF9B9B)
val LightPastelRed = Color(0xFFFFBDBD)
val LighterPastelRed = Color(0xFFFFDEDE)
val DarkRed = Color(0xFF8B0000)

// Нейтральные цвета
val White = Color(0xFFFFFFFF)
val LightGray = Color(0xFFF5F5F5)
val Gray = Color(0xFF888888)
val DarkGray = Color(0xFF333333)
val Black = Color(0xFF000000)
val DarkBackground = Color(0xFF121212)

// Новые современные цвета
val MintGreen = Color(0xFF23C9B6)
val DeepPurple = Color(0xFF5E35B1)
val CoralPink = Color(0xFFF8BBD0)
val SunsetOrange = Color(0xFFFF7043)
val LavenderPurple = Color(0xFF9C7CDE)
val SkyBlue = Color(0xFF64B5F6)
val TealBlue = Color(0xFF26A69A)
val AmberYellow = Color(0xFFFFCA28)

// Градиентные и специальные цвета
object GradientColors {
    val BlueGradient = Brush.linearGradient(
        colors = listOf(PastelBlue, LightPastelBlue, LighterPastelBlue)
    )
    
    val PurpleBlueGradient = Brush.linearGradient(
        colors = listOf(DeepPurple, PastelBlue, SkyBlue)
    )
    
    val PinkPurpleGradient = Brush.linearGradient(
        colors = listOf(PastelPink, LavenderPurple, DeepPurple)
    )
    
    val GreenTealGradient = Brush.linearGradient(
        colors = listOf(PastelGreen, MintGreen, TealBlue)
    )
    
    val SunsetGradient = Brush.linearGradient(
        colors = listOf(SunsetOrange, AmberYellow, CoralPink)
    )
}

// Цвета для различных состояний и элементов UI
val SuccessColor = PastelGreen
val ErrorColor = PastelRed
val WarningColor = AmberYellow
val InfoColor = SkyBlue 